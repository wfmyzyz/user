package com.wfmyzyz.user.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wfmyzyz.user.user.domain.Authority;
import com.wfmyzyz.user.user.domain.RoleAuthority;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author auto
 * @since 2019-12-30
 */
public interface IRoleAuthorityService extends IService<RoleAuthority> {

    /**
     * 根据权限ID删除角色与权限绑定
     * @param authorityIds
     * @return
     */
    boolean removeByAuthorityIds(List<Integer> authorityIds);

    /**
     * 根据角色ID删除角色与权限绑定
     * @param roleIdList
     * @return
     */
    boolean removeByRoleIds(List<Integer> roleIdList);

    /**
     * 根据request获取可操作的权限ID
     * @param request
     * @return
     */
    List<Integer> getCanOperationAuthorityIds(HttpServletRequest request);

    /**
     * 根据用户ID获取可操作的权限ID
     * @param userId
     * @return
     */
    List<Integer> getCanOperationAuthorityIds(Integer userId);

    /**
     * 根据角色ID列表获取权限列表
     * @param roleIdList
     * @return
     */
    List<RoleAuthority> getAuthorityListByRoleIds(List<Integer> roleIdList);

    /**
     * 根据角色ID列表获取权限列表
     * @param roleId
     * @return
     */
    List<RoleAuthority> getAuthorityListByRoleId(Integer roleId);

    /**
     * 获取可操作的权限ID根据需要添加的与拥有的权限ID
     * @param authorityIdList
     * @param canOperationAuthorityIds
     * @return
     */
    List<Integer> getCanOperationAuthorityIdList(List<Integer> authorityIdList, List<Integer> canOperationAuthorityIds);

    /**
     * 角色绑定权限
     * @param roleId
     * @param authorityList
     * @return
     */
    boolean roleBindAuthority(Integer roleId, List<Authority> authorityList);

    /**
     * 获取角色权限绑定列表根据角色ID
     * @param roleId
     * @return
     */
    List<RoleAuthority> listByRoleId(Integer roleId);

    /**
     * 超级管理员绑定角色权限
     * @param roleId
     * @param authorityIdList
     * @return
     */
    boolean superRoleBindAuthority(Integer roleId, List<Integer> authorityIdList);

    /**
     * 根据角色ID删除绑定权限
     * @param roleId
     * @return
     */
    boolean removeByRoleId(Integer roleId);

    /**
     * 根据角色ID与权限ID列表删除绑定
     * @param roleId
     * @param authorityList
     * @return
     */
    boolean removeByRoleIdAndAuthorityIdList(Integer roleId, List<Authority> authorityList);

    /**
     * 根据用户ID获取角色权限列表
     * @param userId
     * @return
     */
    List<RoleAuthority> getRoleAuthorityList(Integer userId);
}
