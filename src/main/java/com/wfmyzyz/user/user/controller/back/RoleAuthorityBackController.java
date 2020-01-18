package com.wfmyzyz.user.user.controller.back;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wfmyzyz.user.user.domain.Authority;
import com.wfmyzyz.user.user.service.IAuthorityService;
import com.wfmyzyz.user.user.service.IRoleAuthorityService;
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
import java.util.List;

/**
 * @author admin
 */
@Api(tags="后台模块-角色权限管理")
@RestController
@RequestMapping("back/roleAuthority")
public class RoleAuthorityBackController {

    @Autowired
    private IUserRoleService userRoleService;
    @Autowired
    private IRoleAuthorityService roleAuthorityService;
    @Autowired
    private IAuthorityService authorityService;

    @ApiOperation(value="角色绑定权限", notes="角色绑定权限" ,httpMethod="POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name="token",value="token",required=true,paramType="header"),
            @ApiImplicitParam(name="roleId",value="角色ID",required=true,paramType="query",dataType="Integer"),
            @ApiImplicitParam(name="authorityIds",value="权限ID数组",required=true,paramType="query",dataType="Array"),
    })
    @RequestMapping(value = "/roleBindAuthority", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public Msg userBindRole(@RequestBody JSONObject params, HttpServletRequest request){
        Integer roleId = params.getInteger("roleId");
        JSONArray authorityIds = params.getJSONArray("authorityIds");
        List<Integer> authorityIdList = ControllerUtils.jsonArrayChangeIntegerList(authorityIds);
        if (roleId == null || authorityIds == null){
            return Msg.error().add("error","必填字段不能为空");
        }
        List<Integer> canOperationRoleIdNoOnSelf = userRoleService.getCanOperationRoleIdNoOnSelf(request);
        if (canOperationRoleIdNoOnSelf == null || !canOperationRoleIdNoOnSelf.contains(roleId)){
            return Msg.error().add("error","没有权限");
        }
        //如果是超级管理员则跳过下列验证
        if (!userRoleService.checkUserIsSuperAdmin(request)){
            //不是超级管理员
            List<Integer> canOperationAuthorityIds = roleAuthorityService.getCanOperationAuthorityIds(request);
            if (canOperationAuthorityIds == null || canOperationAuthorityIds.size() <= 0){
                return Msg.error().add("error","没有权限");
            }
            List<Integer> canOperationAuthorityIdList = roleAuthorityService.getCanOperationAuthorityIdList(authorityIdList, canOperationAuthorityIds);
            if (canOperationAuthorityIdList == null || canOperationAuthorityIdList.size() <= 0){
                return Msg.error().add("error","没有权限");
            }
            List<Authority> authorityList = authorityService.listByIds(canOperationAuthorityIdList);
            if (authorityList == null || authorityList.size() <= 0){
                return Msg.error().add("error","没有该权限");
            }
            if (authorityIdList.size() <= 0){
                roleAuthorityService.removeByRoleIdAndAuthorityIdList(roleId,authorityList);
            }else {
                roleAuthorityService.roleBindAuthority(roleId,authorityList);
            }
        }else {
            //是超级管理员
            roleAuthorityService.superRoleBindAuthority(roleId,authorityIdList);
        }
        return Msg.success().add("data","绑定成功");
    }
}
