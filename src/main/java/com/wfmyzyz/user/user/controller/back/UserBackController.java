package com.wfmyzyz.user.user.controller.back;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wfmyzyz.user.user.domain.User;
import com.wfmyzyz.user.user.enums.UserStatusEnum;
import com.wfmyzyz.user.user.service.IUserRoleService;
import com.wfmyzyz.user.user.service.IUserService;
import com.wfmyzyz.user.user.vo.AddUserVo;
import com.wfmyzyz.user.user.vo.UpdateUserPasswordVo;
import com.wfmyzyz.user.utils.ControllerUtils;
import com.wfmyzyz.user.utils.LayuiBackData;
import com.wfmyzyz.user.utils.Msg;
import com.wfmyzyz.user.utils.TokenUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author admin
 */
@Api(tags="后台模块-用户管理")
@RestController
@RequestMapping("back/user")
public class UserBackController {

    @Autowired
    private IUserService userService;
    @Autowired
    private TokenUtils tokenUtils;
    @Autowired
    private IUserRoleService userRoleService;

    @ApiOperation(value="查询用户列表", notes="查询用户列表" ,httpMethod="POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name="token",value="token",required=true,paramType="header"),
            @ApiImplicitParam(name="page",value="页数",required=true,paramType="query",dataType="Integer"),
            @ApiImplicitParam(name="limit",value="页数据数",required=true,paramType="query",dataType="Integer"),
            @ApiImplicitParam(name="userId",value="用户ID",required=false,paramType="query",dataType="Integer"),
            @ApiImplicitParam(name="status",value="用户状态",required=false,paramType="query",dataType="String"),
            @ApiImplicitParam(name="username",value="用户账号",required=false,paramType="query",dataType="String"),
            @ApiImplicitParam(name="startTime",value="开始时间",required=false,paramType="query",dataType="String"),
            @ApiImplicitParam(name="endTime",value="结束时间",required=false,paramType="query",dataType="String"),
    })
    @RequestMapping(value = "/getUserList", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public LayuiBackData getUserList(@RequestParam("page") Integer page,@RequestParam("limit") Integer limit,
                                     @RequestParam(value = "userId",required = false) Integer userId,@RequestParam(value = "status",required = false) String status,
                                     @RequestParam(value = "username",required = false) String username,@RequestParam(value = "startTime",required = false) String startTime,
                                     @RequestParam(value = "endTime",required = false) String endTime){
        IPage<User> userIPage = userService.listByPage(page, limit, userId, username, status, startTime, endTime);
        return LayuiBackData.bringData(userIPage);
    }

    @ApiOperation(value="添加新用户", notes="添加新用户" ,httpMethod="POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name="token",value="token",required=true,paramType="header"),
            @ApiImplicitParam(name="username",value="用户名",required=true,paramType="query",dataType="String"),
            @ApiImplicitParam(name="password",value="密码",required=true,paramType="query",dataType="String"),
            @ApiImplicitParam(name="passwords",value="验证密码",required=true,paramType="query",dataType="String")
    })
    @RequestMapping(value = "/addUser", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public Msg addUser(@Valid @RequestBody AddUserVo userVo){
        if (!Objects.equals(userVo.getPassword(),userVo.getPasswords())){
            return Msg.error().add("error","两次密码不一致");
        }
        //此处可以选择使用rsa解密密码，如果前端加密的话
        User user = userService.getUserByUsername(userVo.getUsername());
        if (user != null){
            return Msg.error().add("error","用户名已存在");
        }
        boolean flag = userService.addUser(userVo);
        if (!flag){
            return Msg.error().add("error","添加失败");
        }
        return Msg.success().add("data","添加成功");
    }

    @ApiOperation(value="修改用户密码", notes="修改用户密码" ,httpMethod="POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name="token",value="token",required=true,paramType="header"),
            @ApiImplicitParam(name="oldPassword",value="旧密码",required=true,paramType="query",dataType="String"),
            @ApiImplicitParam(name="newPassword",value="新密码",required=true,paramType="query",dataType="String"),
            @ApiImplicitParam(name="newPasswords",value="新验证密码",required=true,paramType="query",dataType="String")
    })
    @RequestMapping(value = "/updatePassword", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public Msg updatePassword(@Valid @RequestBody UpdateUserPasswordVo updateUserPasswordVo, HttpServletRequest request){
        Integer userId = tokenUtils.getUserIdByToken(request);
        User user = userService.getById(userId);
        if (user == null){
            return Msg.error().add("error","用户不存在！");
        }
        String md5OldPasswordSaltStr = userService.getStrByPasswordAndSalt(updateUserPasswordVo.getOldPassword(),user.getSalt());
        if (!Objects.equals(md5OldPasswordSaltStr,user.getPassword())){
            return Msg.error().add("error","用户旧密码错误！");
        }
        boolean flag = userService.updateUserPassword(user,updateUserPasswordVo.getNewPassword(),userId);
        if (!flag){
            return Msg.error().add("error","修改失败");
        }
        return Msg.success().add("data","修改成功");
    }

    @ApiOperation(value="根据用户ID修改用户密码", notes="根据用户ID修改用户密码" ,httpMethod="POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name="token",value="token",required=true,paramType="header"),
            @ApiImplicitParam(name="userId",value="用户ID",required=true,paramType="query",dataType="String"),
            @ApiImplicitParam(name="newPassword",value="新密码",required=true,paramType="query",dataType="String"),
            @ApiImplicitParam(name="newPasswords",value="新验证密码",required=true,paramType="query",dataType="String")
    })
    @RequestMapping(value = "/updatePasswordByUserId", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public Msg updatePasswordByUserId(@RequestBody JSONObject params, HttpServletRequest request){
        Integer userId = params.getInteger("userId");
        String newPassword = params.getString("password");
        String newPasswords = params.getString("passwords");
        if (userId == null || StringUtils.isBlank(newPassword) || StringUtils.isBlank(newPasswords)){
            return Msg.error().add("error","必填项不能为空");
        }
        if (!Objects.equals(newPassword,newPasswords)){
            return Msg.error().add("error","两次密码不一致");
        }
        if (!userService.checkAuthority(request,userId)){
            return Msg.error().add("error","没有权限");
        }
        User user = userService.getById(userId);
        if (user == null){
            return Msg.error().add("error","用户不存在！");
        }
        boolean flag = userService.updateUserPassword(user,newPassword,userId);
        if (!flag){
            return Msg.error().add("error","修改失败");
        }
        return Msg.success().add("data","修改成功");
    }

    @ApiOperation(value="修改用户账号", notes="修改用户账号" ,httpMethod="POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name="token",value="token",required=true,paramType="header"),
            @ApiImplicitParam(name="status",value="状态",required=true,paramType="query",dataType="String"),
            @ApiImplicitParam(name="userId",value="用户ID",required=true,paramType="query",dataType="Integer")
    })
    @RequestMapping(value = "/updateUserStatus", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public Msg updateUserStatus(@RequestBody JSONObject params, HttpServletRequest request){
        String status = params.getString("status");
        Integer userId = params.getInteger("userId");
        if (StringUtils.isBlank(status) || userId == null){
            return Msg.error().add("error","参数不能为空");
        }
        if (!Objects.equals(status, UserStatusEnum.正常.toString()) && !Objects.equals(status,UserStatusEnum.冻结.toString())){
            return Msg.error().add("error","状态格式错误");
        }
        if (!userService.checkAuthority(request,userId)){
            return Msg.error().add("error","没有权限");
        }
        boolean flag = userService.updateUserStatus(userId, status);
        if (!flag){
            return Msg.error().add("error","修改用户状态错误");
        }
        return Msg.success().add("data","修改用户状态成功");
    }

    @ApiOperation(value="删除用户账号", notes="删除用户账号" ,httpMethod="POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name="token",value="token",required=true,paramType="header"),
            @ApiImplicitParam(name="userIds",value="用户数组ID",required=true,paramType="query",dataType="List")
    })
    @Transactional
    @RequestMapping(value = "/deleteUser", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public Msg deleteUser(@RequestBody JSONArray userIds,HttpServletRequest request){
        if (userIds == null || userIds.size() <=0){
            return Msg.error().add("error","参数错误");
        }
        List<Integer> userIdList = ControllerUtils.jsonArrayChangeIntegerList(userIds);
        List<Integer> deleteUserIdList = userIdList.stream().filter(userId -> userService.checkAuthority(request, userId)).collect(Collectors.toList());
        if (deleteUserIdList.size() <= 0){
            return Msg.error().add("error","没有权限");
        }
        boolean flag = userService.removeByIds(deleteUserIdList);
        if (!flag){
            return Msg.error().add("data","删除失败");
        }
        userRoleService.removeByUserIdList(deleteUserIdList);
        return Msg.success().add("data","删除成功");
    }

}
