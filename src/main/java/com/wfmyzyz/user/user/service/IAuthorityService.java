package com.wfmyzyz.user.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wfmyzyz.user.user.domain.Authority;
import com.wfmyzyz.user.user.domain.Role;
import com.wfmyzyz.user.user.vo.AddAuthorityVo;
import com.wfmyzyz.user.user.vo.AuthorityTreeVo;
import com.wfmyzyz.user.user.vo.TreeVo;
import com.wfmyzyz.user.user.vo.UpdateAuthorityVo;

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
public interface IAuthorityService extends IService<Authority> {

    /**
     * 添加权限
     * @param addAuthorityVo
     * @return
     */
    boolean addAuthority(AddAuthorityVo addAuthorityVo);

    /**
     * 修改权限
     * @param updateAuthorityVo
     * @return
     */
    boolean updateAuthority(UpdateAuthorityVo updateAuthorityVo);

    /**
     * 根据url查询权限
     * @param url
     * @return
     */
    Authority getAuthorityByUrl(String url);

    /**
     * 获取权限树状列表
     * @return
     */
    List<TreeVo> listTree();

    /**
     * 根据权限ID列表获取子权限ID列表
     * @param authorityIdList
     * @return
     */
    List<Integer> getZAuthorityIdListByAuthorityIdList(List<Integer> authorityIdList);

    /**
     * 根据userId获取TreeVo,判断是否为超级管理员，超级管理员给予所有权限
     * @param roleList
     * @return
     */
    List<TreeVo> getAuthorityListByUserId(List<Role> roleList);

    /**
     * 获取权限列表根据角色ID列表
     * @param roleIdList
     * @return
     */
    List<Authority> listByRoleIdList(List<Integer> roleIdList);

    /**
     * 检查角色是否已经有权限
     * @param list
     * @param authorityIdList
     */
    void checkAuthorityTreeIsHaveByRoleAuthority(List<TreeVo> list, List<Integer> authorityIdList);

    /**
     * 通过用户ID获取权限列表
     * @param userId
     * @return
     */
    List<Authority> getAuthorityList(Integer userId);

    /**
     * 通过request获取权限列表
     * @param request
     * @return
     */
    List<Authority> getAuthorityList(HttpServletRequest request);

    /**
     * 根据request获取菜单列表
     * @param request
     * @return
     */
    List<AuthorityTreeVo> getMenuListByUserId(HttpServletRequest request);
}
