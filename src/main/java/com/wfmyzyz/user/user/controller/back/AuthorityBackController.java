package com.wfmyzyz.user.user.controller.back;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wfmyzyz.user.user.domain.Authority;
import com.wfmyzyz.user.user.domain.Role;
import com.wfmyzyz.user.user.domain.RoleAuthority;
import com.wfmyzyz.user.user.enums.AuthorityTypeEnum;
import com.wfmyzyz.user.user.service.IAuthorityService;
import com.wfmyzyz.user.user.service.IRoleAuthorityService;
import com.wfmyzyz.user.user.service.IRoleService;
import com.wfmyzyz.user.user.service.IUserRoleService;
import com.wfmyzyz.user.user.vo.AddAuthorityVo;
import com.wfmyzyz.user.user.vo.DTree;
import com.wfmyzyz.user.user.vo.TreeVo;
import com.wfmyzyz.user.user.vo.UpdateAuthorityVo;
import com.wfmyzyz.user.utils.ControllerUtils;
import com.wfmyzyz.user.utils.Msg;
import com.wfmyzyz.user.utils.TokenUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author admin
 */
@Api(tags="后台模块-权限管理")
@RestController
@RequestMapping("back/authority")
public class AuthorityBackController {

    @Autowired
    private IAuthorityService authorityService;
    @Autowired
    private IRoleAuthorityService roleAuthorityService;
    @Autowired
    private TokenUtils tokenUtils;
    @Autowired
    private IRoleService roleService;
    @Autowired
    private IUserRoleService userRoleService;

    @ApiOperation(value="查询树形权限列表", notes="查询树形权限列表" ,httpMethod="POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name="token",value="token",required=true,paramType="header")
    })
    @RequestMapping(value = "/getAuthorityTreeList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public DTree getAuthorityTreeList(){
        List<TreeVo> list = authorityService.listTree();
        return DTree.successRoleTree(list);
    }

    @ApiOperation(value="查询树形权限列表", notes="查询树形权限列表" ,httpMethod="POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name="token",value="token",required=true,paramType="header")
    })
    @RequestMapping(value = "/getAuthorityTreeByUserId", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public Msg getAuthorityTreeByUserId(@RequestBody JSONObject params, HttpServletRequest request){
        Integer roleId = params.getInteger("roleId");
        Integer userId = tokenUtils.getUserIdByToken(request);
        List<Role> roleList = roleService.getRoleListByUserId(userId);
        if (roleList == null || roleList.size() <= 0){
            return Msg.error().add("error","没有权限");
        }
        if (!userRoleService.checkUserIsSuperAdmin(request)){
            List<Integer> canOperationRoleIdNoOnSelf = userRoleService.getCanOperationRoleIdNoOnSelf(request);
            if (canOperationRoleIdNoOnSelf == null || !canOperationRoleIdNoOnSelf.contains(roleId)){
                return Msg.error().add("error","没有权限");
            }
        }
        List<TreeVo> list = authorityService.getAuthorityListByUserId(roleList);
        if (list == null){
            return Msg.error().add("error","没有权限");
        }
        List<RoleAuthority> authorityList = roleAuthorityService.getAuthorityListByRoleId(roleId);
        List<Integer> authorityIdList = authorityList.stream().map(RoleAuthority::getAuthorityId).collect(Collectors.toList());
        authorityService.checkAuthorityTreeIsHaveByRoleAuthority(list,authorityIdList);
        return Msg.success().add("data",DTree.successRoleTree(list));
    }

    @ApiOperation(value="根据权限ID获取权限", notes="根据权限ID获取权限" ,httpMethod="POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name="token",value="token",required=true,paramType="header"),
            @ApiImplicitParam(name="authorityId",value="authorityId",required=true,paramType="Integer")
    })
    @RequestMapping(value = "/getAuthorityById", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public Msg getAuthorityById(@RequestBody JSONObject params){
        Integer authorityId = params.getInteger("authorityId");
        if (authorityId == null){
            Msg.error().add("error","权限ID为空");
        }
        Authority authority = authorityService.getById(authorityId);
        if (authority == null){
            Msg.error().add("error","暂无该权限");
        }
        return Msg.success().add("data",authority);
    }

    @ApiOperation(value="添加新权限", notes="添加新权限" ,httpMethod="POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name="token",value="token",required=true,paramType="header"),
            @ApiImplicitParam(name="name",value="权限名",required=true,paramType="query",dataType="String"),
            @ApiImplicitParam(name="url",value="权限地址",required=true,paramType="query",dataType="String"),
            @ApiImplicitParam(name="type",value="权限类型",required=true,paramType="query",dataType="String"),
            @ApiImplicitParam(name="fAuthorityId",value="权限ID",required=true,paramType="query",dataType="Integer")
    })
    @RequestMapping(value = "/addAuthority", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public Msg addAuthority(@Valid @RequestBody AddAuthorityVo addAuthorityVo, HttpServletRequest request){
        if (!Objects.equals(addAuthorityVo.getType(), AuthorityTypeEnum.页面.toString()) && !Objects.equals(addAuthorityVo.getType(),AuthorityTypeEnum.按钮.toString())){
            return Msg.error().add("error","状态格式错误");
        }
        Authority authorityByUrl = authorityService.getAuthorityByUrl(addAuthorityVo.getUrl());
        if (authorityByUrl != null){
            return Msg.error().add("error","添加失败，该权限已存在");
        }
        if (Objects.equals(addAuthorityVo.getfAuthorityId(),0) && !Objects.equals(addAuthorityVo.getType(),AuthorityTypeEnum.页面.toString())){
            return Msg.error().add("error","添加失败，根权限不能为按钮");
        }
        boolean flag = authorityService.addAuthority(addAuthorityVo);
        if (!flag){
            return Msg.error().add("error","添加失败");
        }
        return Msg.success().add("data","添加成功");
    }

    @ApiOperation(value="修改权限", notes="修改权限" ,httpMethod="POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name="token",value="token",required=true,paramType="header"),
            @ApiImplicitParam(name="authorityId",value="权限ID",required=true,paramType="query",dataType="String"),
            @ApiImplicitParam(name="authorityName",value="权限名称",required=true,paramType="query",dataType="String"),
            @ApiImplicitParam(name="authorityUrl",value="权限地址",required=true,paramType="query",dataType="String"),
            @ApiImplicitParam(name="authorityType",value="权限类型",required=true,paramType="query",dataType="String")
    })
    @RequestMapping(value = "/updateAuthority", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public Msg updateAuthority(@Valid @RequestBody UpdateAuthorityVo updateAuthorityVo, HttpServletRequest request){
        if (StringUtils.isBlank(updateAuthorityVo.getAuthorityName()) && StringUtils.isBlank(updateAuthorityVo.getAuthorityUrl()) && StringUtils.isBlank(updateAuthorityVo.getAuthorityType())){
            return Msg.error().add("error","您没有修改权限信息");
        }
        Authority authorityByUrl = authorityService.getAuthorityByUrl(updateAuthorityVo.getAuthorityUrl());
        if (authorityByUrl != null && !Objects.equals(authorityByUrl.getAuthorityId(),updateAuthorityVo.getAuthorityId())){
            return Msg.error().add("error","修改失败，该权限已存在");
        }
        Authority authority = authorityService.getById(updateAuthorityVo.getAuthorityId());
        if (Objects.equals(authority.getFAuthorityId(),0) && updateAuthorityVo.getAuthorityType() != null && !Objects.equals(updateAuthorityVo.getAuthorityType(),AuthorityTypeEnum.页面.toString())){
            return Msg.error().add("error","修改失败，根权限不能为按钮");
        }
        boolean flag = authorityService.updateAuthority(updateAuthorityVo);
        if (!flag){
            return Msg.error().add("error","修改失败");
        }
        return Msg.success().add("data","修改成功");
    }

    @ApiOperation(value="删除权限", notes="删除权限" ,httpMethod="POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name="token",value="token",required=true,paramType="header"),
            @ApiImplicitParam(name="roleIds",value="角色数组ID",required=true,paramType="query",dataType="List")
    })
    @Transactional
    @RequestMapping(value = "/deleteAuthority", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public Msg deleteAuthority(@RequestBody JSONArray authorityIds, HttpServletRequest request){
        if (authorityIds == null || authorityIds.size() <=0){
            return Msg.error().add("error","参数错误");
        }
        List<Integer> authorityIdList = ControllerUtils.jsonArrayChangeIntegerList(authorityIds);
        List<Integer> canDeleteList = authorityService.getZAuthorityIdListByAuthorityIdList(authorityIdList);
        boolean flag = authorityService.removeByIds(canDeleteList);
        if (!flag){
            return Msg.error().add("error","删除失败");
        }
        roleAuthorityService.removeByAuthorityIds(authorityIdList);
        return Msg.success().add("data","删除成功");
    }
}
