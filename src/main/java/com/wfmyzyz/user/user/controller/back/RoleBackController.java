package com.wfmyzyz.user.user.controller.back;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wfmyzyz.user.user.domain.UserRole;
import com.wfmyzyz.user.user.service.IRoleAuthorityService;
import com.wfmyzyz.user.user.service.IRoleService;
import com.wfmyzyz.user.user.service.IUserRoleService;
import com.wfmyzyz.user.user.service.IUserService;
import com.wfmyzyz.user.user.vo.AddRoleVo;
import com.wfmyzyz.user.user.vo.DTree;
import com.wfmyzyz.user.user.vo.TreeVo;
import com.wfmyzyz.user.user.vo.UpdateRoleVo;
import com.wfmyzyz.user.utils.ControllerUtils;
import com.wfmyzyz.user.utils.Msg;
import com.wfmyzyz.user.utils.TokenUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author admin
 */
@Api(tags="后台模块-角色管理")
@RestController
@RequestMapping("back/role")
public class RoleBackController {

    @Autowired
    private IRoleService roleService;
    @Autowired
    private IRoleAuthorityService roleAuthorityService;
    @Autowired
    private IUserRoleService userRoleService;
    @Autowired
    private TokenUtils tokenUtils;
    @Autowired
    private IUserService userService;


    @ApiOperation(value="查询树形角色列表", notes="查询树形角色列表" ,httpMethod="POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name="token",value="token",required=true,paramType="header")
    })
    @RequestMapping(value = "/getRoleTreeList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public DTree getRoleTreeList(HttpServletRequest request){
        Integer userId = tokenUtils.getUserIdByToken(request);
        List<TreeVo> list = roleService.listTree(userId);
        return DTree.successRoleTree(list);
    }

    @ApiOperation(value="查询树形用户角色列表", notes="根据request与传来的userId" ,httpMethod="POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name="token",value="token",required=true,paramType="header"),
            @ApiImplicitParam(name="userId",value="用户ID",required=true,paramType="String")
    })
    @RequestMapping(value = "/getRoleTreeListByUserId", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public Msg getRoleTreeListByUserId(@RequestBody JSONObject params, HttpServletRequest request){
        Integer userId = params.getInteger("userId");
        if (userId == null){
            return Msg.error().add("error","用户ID不能为空");
        }
        if (!userService.checkAuthority(request,userId)){
            return Msg.error().add("error","没有权限");
        }
        Integer operationUserId = tokenUtils.getUserIdByToken(request);
        List<UserRole> userRoleListByUserId = userRoleService.getUserRoleListByUserId(userId);
        List<Integer> userRoleIdList = userRoleListByUserId.stream().map(UserRole::getRoleId).collect(Collectors.toList());
        List<TreeVo> list = roleService.listTreeNoOnSelf(operationUserId);
        roleService.checkRoleTreeIsHaveByUserRoleList(list,userRoleIdList);
        return Msg.success().add("data",DTree.successRoleTree(list));
    }


    @ApiOperation(value="添加新角色", notes="添加新角色" ,httpMethod="POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name="token",value="token",required=true,paramType="header"),
            @ApiImplicitParam(name="name",value="角色名",required=true,paramType="query",dataType="String"),
            @ApiImplicitParam(name="fRoleId",value="密码",required=true,paramType="query",dataType="String")
    })
    @RequestMapping(value = "/addRole", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public Msg addRole(@Valid @RequestBody AddRoleVo addRoleVo, HttpServletRequest request){
        List<Integer> canOperationRoleIdOnSelf = userRoleService.getCanOperationRoleIdOnSelf(request);
        if (canOperationRoleIdOnSelf == null || !canOperationRoleIdOnSelf.contains(addRoleVo.getfRoleId())){
            return Msg.error().add("error","没有权限");
        }
        Integer roleId = roleService.addRole(addRoleVo);
        if (roleId == null){
            return Msg.error().add("error","添加失败");
        }
        return Msg.success().add("data","添加成功");
    }

    @ApiOperation(value="修改角色", notes="修改角色" ,httpMethod="POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name="token",value="token",required=true,paramType="header"),
            @ApiImplicitParam(name="roleId",value="角色ID",required=true,paramType="query",dataType="String"),
            @ApiImplicitParam(name="roleName",value="角色名",required=true,paramType="query",dataType="String"),
    })
    @RequestMapping(value = "/updateRoleName", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public Msg updateRoleName(@Valid @RequestBody UpdateRoleVo updateRoleVo, HttpServletRequest request){
        List<Integer> canOperationRoleIdNoOnSelf = userRoleService.getCanOperationRoleIdNoOnSelf(request);
        if (canOperationRoleIdNoOnSelf == null || !canOperationRoleIdNoOnSelf.contains(updateRoleVo.getRoleId())){
            return Msg.error().add("error","没有权限");
        }
        boolean flag = roleService.updateRole(updateRoleVo);
        if (!flag){
            return Msg.error().add("error","修改失败");
        }
        return Msg.success().add("data","修改成功");
    }

    @ApiOperation(value="删除角色", notes="删除角色" ,httpMethod="POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name="token",value="token",required=true,paramType="header"),
            @ApiImplicitParam(name="roleIds",value="角色数组ID",required=true,paramType="query",dataType="List")
    })
    @Transactional
    @RequestMapping(value = "/deleteRole", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public Msg deleteRole(@RequestBody JSONArray roleIds, HttpServletRequest request){
        if (roleIds == null || roleIds.size() <=0){
            return Msg.error().add("error","参数错误");
        }
        List<Integer> roleIdList = ControllerUtils.jsonArrayChangeIntegerList(roleIds);
        List<Integer> canOperationRoleIdNoOnSelf = userRoleService.getCanOperationRoleIdNoOnSelf(request);
        if (canOperationRoleIdNoOnSelf == null || canOperationRoleIdNoOnSelf.size() <= 0){
            return Msg.error().add("error","没有权限");
        }
        List<Integer> canRemoveIds = new ArrayList<>();
        roleIdList.forEach(id->{
            if (canOperationRoleIdNoOnSelf.contains(id)){
                canRemoveIds.add(id);
            }
        });
        if (canRemoveIds.size() <= 0){
            return Msg.error().add("data","删除失败");
        }
        List<Integer> allSonRoleIdListByRoleIdList = roleService.getAllSonRoleIdListByRoleIdListOnSelf(canRemoveIds);
        boolean flag = roleService.removeByIds(allSonRoleIdListByRoleIdList);
        if (!flag){
            return Msg.error().add("data","删除失败");
        }
        roleAuthorityService.removeByRoleIds(roleIdList);
        userRoleService.removeByRoleIds(roleIdList);
        return Msg.success().add("data","删除成功");
    }
}
