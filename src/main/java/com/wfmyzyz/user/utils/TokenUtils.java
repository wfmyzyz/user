package com.wfmyzyz.user.utils;

import com.alibaba.fastjson.JSONObject;
import com.wfmyzyz.user.config.ProjectConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author admin
 */
@Component
public class TokenUtils {

    private static final Logger logger = LoggerFactory.getLogger(TokenUtils.class);

    @Autowired
    private RedisUtils redisUtils;


    /**
     * 根据id创建token
     * @param id
     * @return
     */
    public String createToken(Integer id){
        String nowDatetime = DatetimeUtils.getNowDatetimeStr();
        String userIdStr = "userId["+id+"]";
        String tokenStr = nowDatetime + "-" + userIdStr;
        String base64EncodeTokenStr = Base64.encodeBase64String(tokenStr.getBytes());
        HashMap<String,String> keyMap = redisUtils.getValue(ProjectConfig.PUBLIC_PREFIX, HashMap.class);
        if (keyMap == null){
            return null;
        }
        String publicKey = keyMap.get(RsaUtils.PUBLIC_KEY);
        String token = null;
        try {
            token = RsaUtils.encrypt(base64EncodeTokenStr, publicKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return token;
    }

    /**
     * 根据token获取用户ID
     * @param request
     * @return
     */
    public Integer getUserIdByToken(HttpServletRequest request){
        String token = request.getHeader(ProjectConfig.TOKEN_KEY);
        return getUserIdByToken(token);
    }

    /**
     * 根据token获取用户ID
     * @param token
     * @return
     */
    public Integer getUserIdByToken(String token){
        HashMap<String,String> keyMap = redisUtils.getValue(ProjectConfig.PUBLIC_PREFIX, HashMap.class);
        if (keyMap == null){
            return null;
        }
        String decryptToken;
        try {
            decryptToken = RsaUtils.decrypt(token, keyMap.get(RsaUtils.PRIVATE_KEY));
        } catch (Exception e) {
            logger.info("token={"+token+"},解密失败");
            return null;
        }
        String base64decodeToken = new String(Base64.decodeBase64(decryptToken));
        String userId = base64decodeToken.substring(base64decodeToken.indexOf("[") + 1, base64decodeToken.lastIndexOf("]"));
        return Integer.parseInt(userId);
    }


    public boolean tokenIsExceed(HttpServletRequest request) {
        String token = request.getHeader(ProjectConfig.TOKEN_KEY);
        if (StringUtils.isBlank(token)){
            return true;
        }
        String redisToken = redisUtils.getValue(ProjectConfig.TOKEN + token);
        if (StringUtils.isBlank(redisToken)){
            return true;
        }
        return false;
    }

    public void extendToken(HttpServletRequest request) {
        Integer userId = getUserIdByToken(request);
        String token = request.getHeader(ProjectConfig.TOKEN_KEY);
        if (StringUtils.isNotBlank(token) && userId != null){
            redisUtils.setKeyExpire(ProjectConfig.USER + userId,30, TimeUnit.MINUTES);
            redisUtils.setKeyExpire(ProjectConfig.TOKEN+token,30, TimeUnit.MINUTES);
        }
    }
}
