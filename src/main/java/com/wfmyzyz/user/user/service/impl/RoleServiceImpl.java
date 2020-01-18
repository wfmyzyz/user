package com.wfmyzyz.user.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wfmyzyz.user.user.domain.Role;
import com.wfmyzyz.user.user.domain.UserRole;
import com.wfmyzyz.user.user.mapper.RoleMapper;
import com.wfmyzyz.user.user.service.IRoleService;
import com.wfmyzyz.user.user.service.IUserRoleService;
import com.wfmyzyz.user.user.vo.AddRoleVo;
import com.wfmyzyz.user.user.vo.TreeVo;
import com.wfmyzyz.user.user.vo.UpdateRoleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author auto
 * @since 2019-12-27
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    @Autowired
    private IUserRoleService userRoleService;

    @Override
    public Integer addRole(AddRoleVo addRoleVo) {
        Role role = new Role();
        role.setName(addRoleVo.getName());
        role.setfRoleId(addRoleVo.getfRoleId());
        boolean flag = this.save(role);
        if (!flag){
            return null;
        }
        return role.getRoleId();
    }

    @Override
    public boolean updateRole(UpdateRoleVo updateRoleVo) {
        Role role = new Role();
        role.setRoleId(updateRoleVo.getRoleId());
        role.setName(updateRoleVo.getRoleName());
        return this.updateById(role);
    }

    @Override
    public List<Role> findRoleListByFRoleId(Integer fRoleId) {
        List<Role> roleList = this.list();
        List<Role> zRoleList = new ArrayList<>();
        recursionFindRoleIdByFRoleId(roleList,zRoleList,fRoleId);
        return zRoleList;
    }

    @Override
    public List<TreeVo> listTree(Integer userId) {
        List<UserRole> userRoleListByUserId = userRoleService.getUserRoleListByUserId(userId);
        List<Role> roleList = this.list();
        List<TreeVo> resultTreeVoList = new ArrayList<>();
        userRoleListByUserId.forEach(userRole -> {
            Role role = this.getById(userRole.getRoleId());
            List<TreeVo> treeVoList = recursionFindRoleTreeVoListByFRoleId(roleList, userRole.getRoleId());
            TreeVo treeVosList = createRoleTreeVo(role, treeVoList);
            resultTreeVoList.add(treeVosList);
        });
        return resultTreeVoList;
    }

    @Override
    public List<TreeVo> listTreeNoOnSelf(Integer userId) {
        List<UserRole> userRoleListByUserId = userRoleService.getUserRoleListByUserId(userId);
        List<Role> roleList = this.list();
        List<TreeVo> resultTreeVoList = new ArrayList<>();
        userRoleListByUserId.forEach(userRole -> {
            List<TreeVo> treeVoList = recursionFindRoleTreeVoListByFRoleId(roleList, userRole.getRoleId());
            resultTreeVoList.addAll(treeVoList);
        });
        return resultTreeVoList;
    }

    @Override
    public List<Integer> getAllSonRoleIdListByRoleIdList(List<Integer> roleIdList) {
        if (roleIdList == null || roleIdList.size() <=0 ){
            return null;
        }
        List<Role> roleList = this.list();
        List<Integer> sonRoleIdList = new ArrayList<>();
        roleIdList.forEach(roleId->{
            List<Role> zRoleList = new ArrayList<>();
            recursionFindRoleIdByFRoleId(roleList,zRoleList,roleId);
            sonRoleIdList.addAll(zRoleList.stream().map(Role::getRoleId).collect(Collectors.toList()));
        });
        List<Integer> noRepeatSonRoleIdList = sonRoleIdList.stream().distinct().collect(Collectors.toList());
        return noRepeatSonRoleIdList;
    }

    @Override
    public List<Integer> getAllSonRoleIdListByRoleIdListOnSelf(List<Integer> canRemoveIds) {
        List<Integer> allSonRoleIdListByRoleIdList = this.getAllSonRoleIdListByRoleIdList(canRemoveIds);
        allSonRoleIdListByRoleIdList.addAll(canRemoveIds);
        return allSonRoleIdListByRoleIdList;
    }

    @Override
    public void checkRoleTreeIsHaveByUserRoleList(List<TreeVo> list, List<Integer> userRoleIdList) {
        list.forEach(roleTreeVo -> {
            if (userRoleIdList.contains(roleTreeVo.getId())){
                roleTreeVo.setSelected(true);
            }
            if (roleTreeVo.getChildren() != null && roleTreeVo.getChildren().size() > 0){
                this.checkRoleTreeIsHaveByUserRoleList(roleTreeVo.getChildren(),userRoleIdList);
            }
        });
    }

    @Override
    public List<Role> listByRoleIdList(List<Integer> roleIdList) {
        return this.list(new QueryWrapper<Role>().in("role_id",roleIdList));
    }

    @Override
    public List<Role> getRoleListByUserId(Integer userId) {
        List<UserRole> userRoleList = userRoleService.getUserRoleListByUserId(userId);
        if (userRoleList == null || userRoleList.size() <= 0){
            return null;
        }
        return this.listByRoleIdList(userRoleList.stream().map(UserRole::getRoleId).collect(Collectors.toList()));
    }

    /**
     * 递归寻找子角色封装为角色树
     * @param roleList
     * @param roleId
     */
    private List<TreeVo> recursionFindRoleTreeVoListByFRoleId(List<Role> roleList, Integer roleId){
        List<TreeVo> treeVoList = new ArrayList<>();
        roleList.forEach(role -> {
            if (Objects.equals(role.getfRoleId(),roleId)){
                List<TreeVo> resultTreeVoList = recursionFindRoleTreeVoListByFRoleId(roleList, role.getRoleId());
                treeVoList.add(createRoleTreeVo(role, resultTreeVoList));
            }
        });
        return treeVoList;
    }

    /**
     * 根据角色创建树对象
     * @param role
     * @param treeVoList
     * @return
     */
    private TreeVo createRoleTreeVo(Role role, List<TreeVo> treeVoList){
        TreeVo treeVo = new TreeVo();
        treeVo.setId(role.getRoleId());
        treeVo.setParentId(role.getfRoleId());
        treeVo.setTitle(role.getName());
        treeVo.setChildren(treeVoList);
        return treeVo;
    }

    /**
     * 递归寻找子角色
     * @param roleList
     * @param zRoleList
     * @param roleId
     */
    private void recursionFindRoleIdByFRoleId(List<Role> roleList,List<Role> zRoleList,Integer roleId){
        roleList.forEach(role -> {
            if (Objects.equals(role.getfRoleId(),roleId)){
                recursionFindRoleIdByFRoleId(roleList,zRoleList,role.getRoleId());
                zRoleList.add(role);
            }
        });
    }
}
