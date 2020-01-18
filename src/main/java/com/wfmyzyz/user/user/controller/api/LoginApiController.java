package com.wfmyzyz.user.user.controller.api;

import com.alibaba.fastjson.JSONObject;
import com.wfmyzyz.user.config.ProjectConfig;
import com.wfmyzyz.user.user.domain.User;
import com.wfmyzyz.user.user.enums.UserStatusEnum;
import com.wfmyzyz.user.user.service.IUserService;
import com.wfmyzyz.user.utils.Msg;
import com.wfmyzyz.user.utils.RedisUtils;
import com.wfmyzyz.user.utils.RsaUtils;
import com.wfmyzyz.user.utils.TokenUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author admin
 */
@Api(tags="API模块-登录管理")
@RestController
@RequestMapping("api")
public class LoginApiController {

    @Autowired
    private IUserService userService;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private TokenUtils tokenUtils;

    /**
     * 获取公钥
     * @return
     */
    @ApiOperation(value="获取公钥", notes="获取公钥" ,httpMethod="GET")
    @RequestMapping(value = "/login/getRsaPublicKey", method = RequestMethod.GET)
    public Msg getPublicKey(){
        String publicKey;
        HashMap keyMap = redisUtils.getValue(ProjectConfig.PUBLIC_PREFIX, HashMap.class);
        if (keyMap == null){
            publicKey = getPublicKeyRsa();
            if (StringUtils.isBlank(publicKey)){
                return Msg.success().add("error","获取失败");
            }
        }else {
            publicKey = keyMap.get(RsaUtils.PUBLIC_KEY).toString();
        }
        return Msg.success().add("data",publicKey);
    }

    /**
     * 用户登录
     * @return
     */
    @ApiOperation(value="用户登录", notes="用户登录" ,httpMethod="POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name="username",value="账号",required=true,paramType="query",dataType="String"),
            @ApiImplicitParam(name="password",value="密码",required=true,paramType="query",dataType="String"),
    })
    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public Msg login(@RequestBody JSONObject params){
        String username = params.getString("username");
        String password = params.getString("password");
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)){
            Msg.error().add("error","密码或账号不正确");
        }
        String decryptPassword = decryptPassword(password);
        if (StringUtils.isBlank(decryptPassword)){
            Msg.error().add("error","登录失败，请重新登录");
        }
        User user = userService.checkUser(username, decryptPassword);
        if (user == null || Objects.equals(user.getStatus(), UserStatusEnum.冻结.toString())){
            return Msg.error().add("error","账号或密码不正确");
        }
        String token = tokenUtils.createToken(user.getUserId());
        if (redisUtils.getValue(ProjectConfig.USER+user.getUserId()) != null){
            String oldToken = redisUtils.getValue(ProjectConfig.USER + user.getUserId());
            redisUtils.deletStringeKey(oldToken);
        }
        redisUtils.addToRedis(ProjectConfig.USER+user.getUserId(),ProjectConfig.TOKEN+token,30,TimeUnit.MINUTES);
        redisUtils.addToRedis(ProjectConfig.TOKEN+token,user,30,TimeUnit.MINUTES);
        return Msg.success().add("data",token);
    }

    /**
     * 获取rsa公钥
     * @return
     */
    private String getPublicKeyRsa(){
        Map<String, String> rsaMap = RsaUtils.genKeyPair();
        String publicKey = rsaMap.get(RsaUtils.PUBLIC_KEY);
        String privateKey = rsaMap.get(RsaUtils.PRIVATE_KEY);
        if (StringUtils.isBlank(publicKey) || StringUtils.isBlank(privateKey)){
            return null;
        }
        redisUtils.addToRedis(ProjectConfig.PUBLIC_PREFIX,rsaMap,ProjectConfig.PUBLIC_PRIVATE_KEY_LONG_TIME, TimeUnit.DAYS);
        return publicKey;
    }

    /**
     * 获取解密后的密码
     * @param encryptionPassword
     * @return
     */
    private String decryptPassword(String encryptionPassword){
        HashMap keyMap = redisUtils.getValue(ProjectConfig.PUBLIC_PREFIX, HashMap.class);
        if (keyMap == null){
            return null;
        }
        String privateKey = keyMap.get(RsaUtils.PRIVATE_KEY).toString();
        String decrypt = null;
        try {
            decrypt = RsaUtils.decrypt(encryptionPassword, privateKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decrypt;
    }



}
