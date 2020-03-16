package com.wfmyzyz.user.user.controller.api;


import com.wfmyzyz.user.config.ProjectConfig;
import com.wfmyzyz.user.user.bo.UserBo;
import com.wfmyzyz.user.user.domain.Authority;
import com.wfmyzyz.user.user.domain.User;
import com.wfmyzyz.user.user.service.IAuthorityService;
import com.wfmyzyz.user.user.service.IUserRoleService;
import com.wfmyzyz.user.user.service.IUserService;
import com.wfmyzyz.user.utils.Msg;
import com.wfmyzyz.user.utils.RedisUtils;
import com.wfmyzyz.user.utils.TokenUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author admin
 */
@Api(tags="API模块-服务之间调用")
@RestController
@RequestMapping("api/rest")
public class RestApiController {

    @Autowired
    private TokenUtils tokenUtils;
    @Autowired
    private IUserService userService;
    @Autowired
    private IAuthorityService authorityService;
    @Autowired
    private IUserRoleService userRoleService;
    @Autowired
    private RedisUtils redisUtils;

    /**
     * 外部服务验证权限
     * @param token
     * @return
     */
    @ApiOperation(value="外部服务验证权限", notes="外部服务验证权限" ,httpMethod="POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name="token",value="token",required=true,paramType="query",dataType="String")
    })
    @RequestMapping(value = "/userIsAuthority", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public Msg userIsAuthority(@RequestParam("token") String token,@RequestParam("url") String url){
        if (StringUtils.isBlank(token)){
            return Msg.error("token为空！");
        }
        if (StringUtils.isBlank(url)){
            return Msg.error("url为空！");
        }
        String redisToken = redisUtils.getValue(ProjectConfig.TOKEN + token);
        if (StringUtils.isBlank(redisToken)){
            return Msg.needLogin();
        }
        Integer userId = tokenUtils.getUserIdByToken(token);
        if (userId == null){
            return Msg.error("用户不存在！");
        }
        User user = userService.getById(userId);
        if (user == null){
            return Msg.error("用户不存在！");
        }
        List<Authority> authorityList = authorityService.getAuthorityList(userId);
        UserBo userBo = new UserBo();
        userBo.setUserId(userId);
        userBo.setUsername(user.getUsername());
        if (userRoleService.checkUserIsSuperAdmin(token)){
            userBo.setAuthority(true);
        }else if (authorityList == null){
            userBo.setAuthority(false);
        }else {
            List<String> urlList = authorityList.stream().map(Authority::getUrl).distinct().collect(Collectors.toList());
            if (!urlList.contains(url)){
                userBo.setAuthority(false);
            }else {
                userBo.setAuthority(true);
            }
        }
        return Msg.success().add("data",userBo);
    }
}
