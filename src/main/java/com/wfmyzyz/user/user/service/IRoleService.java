package com.wfmyzyz.user.user.service;

import com.wfmyzyz.user.user.domain.Role;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wfmyzyz.user.user.vo.AddRoleVo;
import com.wfmyzyz.user.user.vo.TreeVo;
import com.wfmyzyz.user.user.vo.UpdateRoleVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author auto
 * @since 2019-12-27
 */
public interface IRoleService extends IService<Role> {

    /**
     * 添加角色
     * @param addRoleVo
     * @return
     */
    Integer addRole(AddRoleVo addRoleVo);

    /**
     * 修改角色
     * @param updateRoleVo
     * @return
     */
    boolean updateRole(UpdateRoleVo updateRoleVo);

    /**
     * 根据父角色ID发现子角色包括自己
     * @param fRoleId
     * @return
     */
    List<Role> findRoleListByFRoleId(Integer fRoleId);

    /**
     * 获取树形角色根据用户ID
     * @param userId
     * @return
     */
    List<TreeVo> listTree(Integer userId);

    /**
     * 获取树形角色根据用户ID不包括自己
     * @param userId
     * @return
     */
    List<TreeVo> listTreeNoOnSelf(Integer userId);

    /**
     * 获取所有子角色ID列表根据传入的角色ID列表
     * @return
     */
    List<Integer> getAllSonRoleIdListByRoleIdList(List<Integer> roleIdList);

    /**
     * 获取所有子角色ID列表根据传入的角色ID列表包括自己
     * @return
     */
    List<Integer> getAllSonRoleIdListByRoleIdListOnSelf(List<Integer> canRemoveIds);

    /**
     * 检查用户是否已经存在角色
     * @param list
     * @param userRoleIdList
     */
    void checkRoleTreeIsHaveByUserRoleList(List<TreeVo> list, List<Integer> userRoleIdList);

    /**
     * 根据角色ID列表获取角色列表
     * @param roleIdList
     * @return
     */
    List<Role> listByRoleIdList(List<Integer> roleIdList);

    /**
     * 根据UserId获取角色列表
     * @param userId
     * @return
     */
    List<Role> getRoleListByUserId(Integer userId);

}
