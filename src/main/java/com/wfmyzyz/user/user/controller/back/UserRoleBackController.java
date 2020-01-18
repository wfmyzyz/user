package com.wfmyzyz.user.user.controller.back;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wfmyzyz.user.user.domain.Role;
import com.wfmyzyz.user.user.domain.UserRole;
import com.wfmyzyz.user.user.service.IRoleService;
import com.wfmyzyz.user.user.service.IUserRoleService;
import com.wfmyzyz.user.utils.ControllerUtils;
import com.wfmyzyz.user.utils.Msg;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author admin
 */
@Api(tags="后台模块-用户绑定角色管理")
@RestController
@RequestMapping("back/userRole")
public class UserRoleBackController {

    @Autowired
    private IUserRoleService userRoleService;
    @Autowired
    private IRoleService roleService;


    @ApiOperation(value="用户绑定角色", notes="用户绑定角色" ,httpMethod="POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name="token",value="token",required=true,paramType="header"),
            @ApiImplicitParam(name="userId",value="用户ID",required=true,paramType="query",dataType="Integer"),
            @ApiImplicitParam(name="roleIds",value="角色ID数组",required=true,paramType="query",dataType="Array"),
    })
    @RequestMapping(value = "/userBindRole", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public Msg userBindRole(@RequestBody JSONObject params, HttpServletRequest request){
        Integer userId = params.getInteger("userId");
        JSONArray roleIds = params.getJSONArray("roleIds");
        if (userId == null || roleIds == null){
            return Msg.error().add("error","必填字段不能为空");
        }
        List<Integer> roleIdList = ControllerUtils.jsonArrayChangeIntegerList(roleIds);
        //判断操作人员的可操作角色权限
        List<Integer> canOperationRoleIdNoOnSelf = userRoleService.getCanOperationRoleIdNoOnSelf(request);
        if (canOperationRoleIdNoOnSelf == null){
            return Msg.error().add("error","没有权限");
        }
        //检查操作人员角色是否大于被操作人员的角色
        List<UserRole> userRoleListByUserId = userRoleService.getUserRoleListByUserId(userId);
        if (!userRoleService.checkUserIsUpdateRole(userRoleListByUserId,canOperationRoleIdNoOnSelf)){
            return Msg.error().add("error","没有权限");
        }
        List<Role> roleList = new ArrayList<>();
        if (roleIdList.size() > 0) {
            List<Integer> canOperationRoleIds = userRoleService.getCanOperationRoleIds(roleIdList, canOperationRoleIdNoOnSelf);
            if (canOperationRoleIds.size() <= 0) {
                return Msg.error().add("error", "没有权限");
            }
            roleList = roleService.listByIds(canOperationRoleIds);
            if (roleList == null || roleList.size() <= 0) {
                return Msg.error().add("error", "没有该角色");
            }
        }
        boolean flag = userRoleService.userBindBatchRole(userId, roleList);
        if (!flag){
            return Msg.error().add("error","绑定失败");
        }
        return Msg.success().add("data","绑定成功");
    }

}
