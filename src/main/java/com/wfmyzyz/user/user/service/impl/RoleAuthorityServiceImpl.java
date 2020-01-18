package com.wfmyzyz.user.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wfmyzyz.user.user.domain.Authority;
import com.wfmyzyz.user.user.domain.RoleAuthority;
import com.wfmyzyz.user.user.domain.UserRole;
import com.wfmyzyz.user.user.mapper.RoleAuthorityMapper;
import com.wfmyzyz.user.user.service.IAuthorityService;
import com.wfmyzyz.user.user.service.IRoleAuthorityService;
import com.wfmyzyz.user.user.service.IUserRoleService;
import com.wfmyzyz.user.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author auto
 * @since 2019-12-30
 */
@Service
public class RoleAuthorityServiceImpl extends ServiceImpl<RoleAuthorityMapper, RoleAuthority> implements IRoleAuthorityService {

    @Autowired
    private TokenUtils tokenUtils;
    @Autowired
    private IUserRoleService userRoleService;
    @Autowired
    private IAuthorityService authorityService;

    @Override
    public boolean removeByAuthorityIds(List<Integer> authorityIds) {
        return this.remove(new QueryWrapper<RoleAuthority>().in("authority_id", authorityIds));
    }

    @Override
    public boolean removeByRoleIds(List<Integer> roleIdList) {
        return this.remove(new QueryWrapper<RoleAuthority>().in("role_id", roleIdList));
    }

    @Override
    public List<Integer> getCanOperationAuthorityIds(HttpServletRequest request) {
        Integer userId = tokenUtils.getUserIdByToken(request);
        return this.getCanOperationAuthorityIds(userId);
    }

    @Override
    public List<Integer> getCanOperationAuthorityIds(Integer userId) {
        List<UserRole> userRoleListByUserId = userRoleService.getUserRoleListByUserId(userId);
        List<Integer> roleIdList = userRoleListByUserId.stream().map(UserRole::getRoleId).collect(Collectors.toList());
        List<RoleAuthority> authorityListByRoleIds = this.getAuthorityListByRoleIds(roleIdList);
        return authorityListByRoleIds.stream().map(RoleAuthority::getAuthorityId).collect(Collectors.toList());
    }

    @Override
    public List<RoleAuthority> getAuthorityListByRoleIds(List<Integer> roleIdList) {
        return this.list(new QueryWrapper<RoleAuthority>().in("role_id",roleIdList));
    }

    @Override
    public List<RoleAuthority> getAuthorityListByRoleId(Integer roleId) {
        List<Integer> roleIdList = new ArrayList<>();
        roleIdList.add(roleId);
        return this.getAuthorityListByRoleIds(roleIdList);
    }

    @Override
    public List<Integer> getCanOperationAuthorityIdList(List<Integer> authorityIdList, List<Integer> canOperationAuthorityIds) {
        if (authorityIdList.size() <= 0){
            return canOperationAuthorityIds;
        }
        return authorityIdList.stream().filter(id->canOperationAuthorityIds.contains(id)).collect(Collectors.toList());
    }

    @Override
    public boolean roleBindAuthority(Integer roleId, List<Authority> authorityList) {
        List<RoleAuthority> roleAuthorityList = new ArrayList<>();
        List<Integer> deleteRoleAuthorityIdList = new ArrayList<>();
        List<RoleAuthority> existRoleAuthorityList = this.listByRoleId(roleId);
        List<Integer> existRoleAuthorityIdList = existRoleAuthorityList.stream().map(RoleAuthority::getAuthorityId).collect(Collectors.toList());
        List<Integer> needAddAuthorityIdList = authorityList.stream().map(Authority::getAuthorityId).collect(Collectors.toList());
        needAddAuthorityIdList.forEach(authorityId->{
            if (!existRoleAuthorityIdList.contains(authorityId)){
                RoleAuthority roleAuthority = new RoleAuthority();
                roleAuthority.setRoleId(roleId);
                roleAuthority.setAuthorityId(authorityId);
                roleAuthorityList.add(roleAuthority);
            }
        });
        existRoleAuthorityList.forEach(roleAuthority->{
            if (!needAddAuthorityIdList.contains(roleAuthority.getAuthorityId())){
                deleteRoleAuthorityIdList.add(roleAuthority.getRoleAuthorityId());
            }
        });
        if (deleteRoleAuthorityIdList.size() > 0){
            this.removeByIds(deleteRoleAuthorityIdList);
        }
        if (roleAuthorityList.size() <= 0){
            return true;
        }
        return this.saveBatch(roleAuthorityList);
    }

    @Override
    public List<RoleAuthority> listByRoleId(Integer roleId) {
        return this.list(new QueryWrapper<RoleAuthority>().eq("role_id",roleId));
    }

    @Override
    public boolean superRoleBindAuthority(Integer roleId, List<Integer> authorityIdList) {
        //authorityIdList长度小于等于0表示删除全部
        if (authorityIdList.size() <= 0){
            return this.removeByRoleId(roleId);
        }
        List<Authority> authorityList = authorityService.listByIds(authorityIdList);
        return this.roleBindAuthority(roleId,authorityList);
    }

    @Override
    public boolean removeByRoleId(Integer roleId) {
        List<Integer> roleIdList = new ArrayList<>();
        roleIdList.add(roleId);
        return this.removeByRoleIds(roleIdList);
    }

    @Override
    public boolean removeByRoleIdAndAuthorityIdList(Integer roleId, List<Authority> authorityList) {
        if (authorityList.size() <= 0){
            return false;
        }
        QueryWrapper<RoleAuthority> queryWrapper = new QueryWrapper();
        queryWrapper.eq("role_id",roleId);
        List<Integer> authorityIdList = authorityList.stream().map(Authority::getAuthorityId).collect(Collectors.toList());
        queryWrapper.in("authority_id",authorityIdList);
        return this.remove(queryWrapper);
    }

    @Override
    public List<RoleAuthority> getRoleAuthorityList(Integer userId) {
        List<UserRole> userRoleList = userRoleService.getUserRoleListByUserId(userId);
        if (userRoleList == null || userRoleList.size() <= 0){
            return null;
        }
        return this.getAuthorityListByRoleIds(userRoleList.stream().map(UserRole::getRoleId).collect(Collectors.toList()));
    }
}
