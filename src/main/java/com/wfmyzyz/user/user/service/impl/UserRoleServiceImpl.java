package com.wfmyzyz.user.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wfmyzyz.user.user.domain.Role;
import com.wfmyzyz.user.user.domain.UserRole;
import com.wfmyzyz.user.user.mapper.UserRoleMapper;
import com.wfmyzyz.user.user.service.IRoleService;
import com.wfmyzyz.user.user.service.IUserRoleService;
import com.wfmyzyz.user.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements IUserRoleService {

    @Autowired
    private IRoleService roleService;
    @Autowired
    private TokenUtils tokenUtils;

    @Override
    public boolean removeByRoleIds(List<Integer> roleIdList) {
        return this.remove(new QueryWrapper<UserRole>().in("role_id",roleIdList));
    }

    @Override
    public boolean insert(Integer userId, Integer roleId) {
        UserRole userRole = new UserRole();
        userRole.setUserId(userId);
        userRole.setRoleId(roleId);
        return this.save(userRole);
    }

    @Override
    public List<Role> getCanOperationRoleIdOnSelf(List<UserRole> list) {
        List<Role> canOperationRoleId = getCanOperationRoleId(list);
        if (canOperationRoleId == null){
            return null;
        }
        List<Role> roleList = roleService.listByIds(list.stream().map(UserRole::getRoleId).collect(Collectors.toList()));
        canOperationRoleId.addAll(roleList);
        return canOperationRoleId;
    }

    @Override
    public List<Role> getCanOperationRoleIdNoOnSelf(List<UserRole> list) {
        List<Role> canOperationRoleId = getCanOperationRoleId(list);
        if (canOperationRoleId == null || canOperationRoleId.size() <= 0){
            return null;
        }
        return canOperationRoleId;
    }

    @Override
    public List<UserRole> getUserRoleListByUserId(Integer userId) {
        return this.list(new QueryWrapper<UserRole>().eq("user_id", userId));
    }

    @Override
    public List<UserRole> getUserRoleListByRequest(HttpServletRequest request) {
        Integer userId = tokenUtils.getUserIdByToken(request);
        return this.getUserRoleListByUserId(userId);
    }

    @Override
    public List<Integer> getCanOperationRoleIdOnSelf(HttpServletRequest request) {
        List<UserRole> userRoleListByRequest = this.getUserRoleListByRequest(request);
        if (userRoleListByRequest == null || userRoleListByRequest.size() <= 0){
            return null;
        }
        List<Role> canOperationRoleIdOnSelf = this.getCanOperationRoleIdOnSelf(userRoleListByRequest);
        if (canOperationRoleIdOnSelf == null || canOperationRoleIdOnSelf.size() <= 0){
            return null;
        }
        return canOperationRoleIdOnSelf.stream().map(Role::getRoleId).collect(Collectors.toList());
    }

    @Override
    public List<Integer> getCanOperationRoleIdNoOnSelf(HttpServletRequest request) {
        List<UserRole> userRoleListByRequest = this.getUserRoleListByRequest(request);
        if (userRoleListByRequest == null || userRoleListByRequest.size() <= 0){
            return null;
        }
        List<Role> canOperationRoleIdNoOnSelf = this.getCanOperationRoleIdNoOnSelf(userRoleListByRequest);
        if (canOperationRoleIdNoOnSelf == null || canOperationRoleIdNoOnSelf.size() <= 0){
            return null;
        }
        return canOperationRoleIdNoOnSelf.stream().map(Role::getRoleId).collect(Collectors.toList());
    }

    @Override
    public boolean checkUserIsUpdateRole(List<UserRole> userRoleListByUserId, List<Integer> canOperationRoleId) {
        for (UserRole userRole : userRoleListByUserId) {
            if (!canOperationRoleId.contains(userRole.getRoleId())){
                return false;
            }
        }
        return true;
    }

    @Override
    public List<Integer> getCanOperationRoleIds(List<Integer> roleIdList, List<Integer> canOperationRoleIdList) {
        return roleIdList.stream().filter(id->canOperationRoleIdList.contains(id)).collect(Collectors.toList());
    }

    @Override
    public boolean userBindBatchRole(Integer userId, List<Role> roleList) {
        if (roleList.size() <= 0){
            return this.removeByUserId(userId);
        }
        List<UserRole> userRoleList = new ArrayList<>();
        List<Integer> deleteUserRoleIdList = new ArrayList<>();
        List<UserRole> userRoleListByUserIdList = this.getUserRoleListByUserId(userId);
        List<Integer> existRoleIdList = userRoleListByUserIdList.stream().map(UserRole::getRoleId).collect(Collectors.toList());
        List<Integer> needAddRoleIdList = roleList.stream().map(Role::getRoleId).collect(Collectors.toList());
        needAddRoleIdList.forEach(roleId -> {
            if (!existRoleIdList.contains(roleId)){
                UserRole userRole = new UserRole();
                userRole.setUserId(userId);
                userRole.setRoleId(roleId);
                userRoleList.add(userRole);
            }
        });
        userRoleListByUserIdList.forEach(userRole->{
            if (!needAddRoleIdList.contains(userRole.getRoleId())){
                deleteUserRoleIdList.add(userRole.getUserRoleId());
            }
        });
        if (deleteUserRoleIdList.size() > 0){
            this.removeByIds(deleteUserRoleIdList);
        }
        if (userRoleList.size() <= 0){
            return true;
        }
        return this.saveBatch(userRoleList);
    }

    @Override
    public boolean removeByUserIdList(List<Integer> deleteUserIdList) {
        return this.remove(new QueryWrapper<UserRole>().in("user_id",deleteUserIdList));
    }

    @Override
    public boolean removeByUserId(Integer userId) {
        return this.remove(new QueryWrapper<UserRole>().eq("user_id",userId));
    }

    @Override
    public boolean checkUserIsSuperAdmin(HttpServletRequest request) {
        Integer userId = tokenUtils.getUserIdByToken(request);
        List<Role> roleList = roleService.getRoleListByUserId(userId);
        if (roleList == null || roleList.size() <= 0){
            return false;
        }
        for (Role role : roleList) {
            if (Objects.equals(role.getfRoleId(),0)){
                return true;
            }
        }
        return false;
    }

    /**
     * 获取可操作角色ID
     * @param list
     * @return
     */
    private List<Role> getCanOperationRoleId(List<UserRole> list){
        if (list == null || list.size() <= 0){
            return null;
        }
        List<Integer> nowRoleIdList = list.stream().map(UserRole::getRoleId).collect(Collectors.toList());
        List<Role> canOperationRoleIdList = new ArrayList<>();
        nowRoleIdList.forEach(e->{
            List<Role> roleListByFRoleId = roleService.findRoleListByFRoleId(e);
            canOperationRoleIdList.addAll(roleListByFRoleId);
        });
        return canOperationRoleIdList;
    }
}
