package com.wfmyzyz.user.user.service;

import com.wfmyzyz.user.user.domain.Role;
import com.wfmyzyz.user.user.domain.UserRole;
import com.baomidou.mybatisplus.extension.service.IService;

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
public interface IUserRoleService extends IService<UserRole> {

    /**
     * 根据角色删除用户与角色绑定
     * @param roleIdList
     * @return
     */
    boolean removeByRoleIds(List<Integer> roleIdList);

    /**
     * 用户绑定角色
     * @param userId
     * @param roleId
     * @return
     */
    boolean insert(Integer userId, Integer roleId);

    /**
     * 获取用户可操作的角色包括自己
     * @param list
     * @return
     */
    List<Role> getCanOperationRoleIdOnSelf(List<UserRole> list);

    /**
     * 获取用户可操作的角色不包括自己
     * @param list
     * @return
     */
    List<Role> getCanOperationRoleIdNoOnSelf(List<UserRole> list);

    /**
     * 根据用户ID找到用户当前角色
     * @param userId
     * @return
     */
    List<UserRole> getUserRoleListByUserId(Integer userId);

    /**
     * 根据用户request找到用户当前角色
     * @param request
     * @return
     */
    List<UserRole> getUserRoleListByRequest(HttpServletRequest request);

    /**
     * 根据request获取可操作的角色ID包括自己
     * @param request
     * @return
     */
    List<Integer> getCanOperationRoleIdOnSelf(HttpServletRequest request);

    /**
     * 根据request获取可操作的角色ID不包括自己
     * @param request
     * @return
     */
    List<Integer> getCanOperationRoleIdNoOnSelf(HttpServletRequest request);

    /**
     * 判断用户是否有权限根据目标角色列表与可操作角色ID
     * @param userRoleListByUserId
     * @param canOperationRoleId
     * @return
     */
    boolean checkUserIsUpdateRole(List<UserRole> userRoleListByUserId, List<Integer> canOperationRoleId);

    /**
     * 返回可操作的角色ID
     * @param roleIdList
     * @param canOperationRoleIdNoOnSelf
     * @return
     */
    List<Integer> getCanOperationRoleIds(List<Integer> roleIdList, List<Integer> canOperationRoleIdNoOnSelf);

    /**
     * 用户绑定角色
     * @param userId
     * @param roleList
     * @return
     */
    boolean userBindBatchRole(Integer userId, List<Role> roleList);

    /**
     * 根据用户ID列表删除绑定数据
     * @param deleteUserIdList
     * @return
     */
    boolean removeByUserIdList(List<Integer> deleteUserIdList);

    /**
     * 根据用户ID删除绑定数据
     * @param userId
     * @return
     */
    boolean removeByUserId(Integer userId);

    /**
     * 根据request判断当前用户是否是超级管理员
     * @param request
     * @return
     */
    boolean checkUserIsSuperAdmin(HttpServletRequest request);
}
