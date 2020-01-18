package com.wfmyzyz.user.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wfmyzyz.user.user.domain.Authority;
import com.wfmyzyz.user.user.domain.Role;
import com.wfmyzyz.user.user.domain.RoleAuthority;
import com.wfmyzyz.user.user.enums.AuthorityTypeEnum;
import com.wfmyzyz.user.user.mapper.AuthorityMapper;
import com.wfmyzyz.user.user.service.IAuthorityService;
import com.wfmyzyz.user.user.service.IRoleAuthorityService;
import com.wfmyzyz.user.user.service.IUserRoleService;
import com.wfmyzyz.user.user.vo.AddAuthorityVo;
import com.wfmyzyz.user.user.vo.AuthorityTreeVo;
import com.wfmyzyz.user.user.vo.TreeVo;
import com.wfmyzyz.user.user.vo.UpdateAuthorityVo;
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
public class AuthorityServiceImpl extends ServiceImpl<AuthorityMapper, Authority> implements IAuthorityService {

    @Autowired
    private IRoleAuthorityService roleAuthorityService;
    @Autowired
    private TokenUtils tokenUtils;
    @Autowired
    private IUserRoleService userRoleService;

    @Override
    public boolean addAuthority(AddAuthorityVo addAuthorityVo) {
        Authority authority = new Authority();
        authority.setName(addAuthorityVo.getName());
        authority.setUrl(addAuthorityVo.getUrl());
        authority.setType(addAuthorityVo.getType());
        authority.setFAuthorityId(addAuthorityVo.getfAuthorityId());
        return this.save(authority);
    }

    @Override
    public boolean updateAuthority(UpdateAuthorityVo updateAuthorityVo) {
        Authority authority = new Authority();
        authority.setAuthorityId(updateAuthorityVo.getAuthorityId());
        authority.setType(updateAuthorityVo.getAuthorityType());
        authority.setUrl(updateAuthorityVo.getAuthorityUrl());
        authority.setName(updateAuthorityVo.getAuthorityName());
        return this.updateById(authority);
    }

    /**
     * 根据URL获取权限
     * @param url
     * @return
     */
    @Override
    public Authority getAuthorityByUrl(String url){
        return this.getOne(new QueryWrapper<Authority>().eq("url", url));
    }

    @Override
    public List<TreeVo> listTree() {
        List<Authority> list = this.list();
        List<TreeVo> treeVoList = new ArrayList<>();
        list.forEach(authority -> {
            if (Objects.equals(authority.getFAuthorityId(),0)){
                List<TreeVo> treeVos = recursionGetTreeVo(list, authority.getAuthorityId());
                treeVoList.add(createTreeVo(authority,treeVos));
            }
        });
        return treeVoList;
    }

    @Override
    public List<Integer> getZAuthorityIdListByAuthorityIdList(List<Integer> authorityIdList) {
        List<Integer> authorityList = new ArrayList<>();
        List<Authority> list = this.list();
        authorityIdList.forEach(authority -> {
            this.recursionGetZAuthorityId(list,authorityList,authority);
        });
        return authorityList;
    }

    @Override
    public List<TreeVo> getAuthorityListByUserId(List<Role> roleList) {
        //首先判断用户是否有超级管理员权限，如果有则返回所有权限，没有则返回已有的权限
        List<Integer> fRoleIdList = roleList.stream().map(Role::getfRoleId).collect(Collectors.toList());
        List<Authority> authorityList;
        if (fRoleIdList.contains(0)){
            authorityList = this.list();
        }else {
            List<Integer> roleIdList = roleList.stream().map(Role::getRoleId).collect(Collectors.toList());
            authorityList = this.listByRoleIdList(roleIdList);
        }
        if (authorityList == null || authorityList.size() <= 0){
            return null;
        }
        List<Integer> fAuthorityIdList = authorityList.stream().map(Authority::getFAuthorityId).collect(Collectors.toList());
        List<Integer> authorityIdList = authorityList.stream().map(Authority::getAuthorityId).collect(Collectors.toList());
        List<Integer> rootAuthorityIdList = fAuthorityIdList.stream().filter(authorityId -> !authorityIdList.contains(authorityId)).distinct().collect(Collectors.toList());
        List<TreeVo> treeVoList = new ArrayList<>();
        for (Integer authorityId : rootAuthorityIdList) {
            treeVoList = recursionGetTreeVo(authorityList, authorityId);
        }
        return treeVoList;
    }



    @Override
    public List<Authority> listByRoleIdList(List<Integer> roleIdList) {
        List<RoleAuthority> authorityListByRoleIds = roleAuthorityService.getAuthorityListByRoleIds(roleIdList);
        if (authorityListByRoleIds == null || authorityListByRoleIds.size() <= 0){
            return null;
        }
        List<Integer> authorityIdList = authorityListByRoleIds.stream().map(RoleAuthority::getAuthorityId).collect(Collectors.toList());
        return this.listByIds(authorityIdList);
    }

    @Override
    public void checkAuthorityTreeIsHaveByRoleAuthority(List<TreeVo> list, List<Integer> authorityIdList) {
        list.forEach(roleTreeVo -> {
            if (authorityIdList.contains(roleTreeVo.getId())){
                roleTreeVo.setSelected(true);
            }
            if (roleTreeVo.getChildren() != null && roleTreeVo.getChildren().size() > 0){
                this.checkAuthorityTreeIsHaveByRoleAuthority(roleTreeVo.getChildren(),authorityIdList);
            }
        });
    }

    @Override
    public List<Authority> getAuthorityList(Integer userId) {
        List<RoleAuthority> roleAuthorityList = roleAuthorityService.getRoleAuthorityList(userId);
        if (roleAuthorityList == null || roleAuthorityList.size() <= 0){
            return null;
        }
        return this.listByIds(roleAuthorityList.stream().map(RoleAuthority::getAuthorityId).collect(Collectors.toList()));
    }

    @Override
    public List<Authority> getAuthorityList(HttpServletRequest request) {
        Integer userId = tokenUtils.getUserIdByToken(request);
        return this.getAuthorityList(userId);
    }

    @Override
    public List<AuthorityTreeVo> getMenuListByUserId(HttpServletRequest request) {
        List<Authority> list;
        LambdaQueryChainWrapper<Authority> authorityLambdaQueryChainWrapper = lambdaQuery();
        if (!userRoleService.checkUserIsSuperAdmin(request)){
            Integer userId = tokenUtils.getUserIdByToken(request);
            List<RoleAuthority> roleAuthorityList = roleAuthorityService.getRoleAuthorityList(userId);
            if (roleAuthorityList == null || roleAuthorityList.size() <= 0){
                return null;
            }
            List<Integer> authorityList = roleAuthorityList.stream().map(RoleAuthority::getAuthorityId).collect(Collectors.toList());
            list = authorityLambdaQueryChainWrapper.eq(Authority::getType, AuthorityTypeEnum.页面.toString()).
                    in(Authority::getAuthorityId, authorityList).list();
        }else {
            list = authorityLambdaQueryChainWrapper.eq(Authority::getType, AuthorityTypeEnum.页面.toString()).list();
        }
        if (list == null || list.size() <= 0){
            return null;
        }
        List<AuthorityTreeVo> authorityTreeVoList = new ArrayList<>();
        list.forEach(authority -> {
            if (Objects.equals(authority.getFAuthorityId(),0)){
                List<AuthorityTreeVo> resultAuthorityTreeVoList = recursionFindAuthorityTreeVo(list, authority.getAuthorityId());
                authorityTreeVoList.add(createAuthorityTreeVo(authority,resultAuthorityTreeVoList));
            }
        });
        return authorityTreeVoList;
    }

    private List<AuthorityTreeVo> recursionFindAuthorityTreeVo(List<Authority> authorityList,Integer fAuthorityId){
        List<AuthorityTreeVo> authorityTreeVoList = new ArrayList<>();
        authorityList.forEach(authority -> {
            if (Objects.equals(authority.getFAuthorityId(),fAuthorityId)){
                List<AuthorityTreeVo> resultAuthorityTreeVoList = recursionFindAuthorityTreeVo(authorityList, authority.getAuthorityId());
                authorityTreeVoList.add(createAuthorityTreeVo(authority,resultAuthorityTreeVoList));
            }
        });
        return authorityTreeVoList;
    }

    /**
     * 创建AuthorityTreeVo类
     * @param authority
     * @param authorityTreeVoList
     * @return
     */
    private AuthorityTreeVo createAuthorityTreeVo(Authority authority,List<AuthorityTreeVo> authorityTreeVoList){
        AuthorityTreeVo authorityTreeVo = new AuthorityTreeVo();
        authorityTreeVo.setAuthority(authority);
        authorityTreeVo.setChildren(authorityTreeVoList);
        return authorityTreeVo;
    }

    /**
     * 递归找到子权限ID
     * @return
     */
    private void recursionGetZAuthorityId(List<Authority> authorityList,List<Integer> authorityIdList,Integer fAuthorityId){
        authorityList.forEach(authority -> {
            if (Objects.equals(authority.getFAuthorityId(),fAuthorityId)){
                authorityIdList.add(authority.getAuthorityId());
                this.recursionGetZAuthorityId(authorityList,authorityIdList,authority.getAuthorityId());
            }
        });
    }

    /**
     * 递归发现子权限
     * @param authorityList
     * @param fAuthorityId
     * @return
     */
    private List<TreeVo> recursionGetTreeVo(List<Authority> authorityList,Integer fAuthorityId){
        List<TreeVo> treeVoList = new ArrayList<>();
        authorityList.forEach(authority -> {
            if (Objects.equals(authority.getFAuthorityId(),fAuthorityId)){
                List<TreeVo> treeVos = recursionGetTreeVo(authorityList, authority.getAuthorityId());
                treeVoList.add(createTreeVo(authority,treeVos));
            }
        });
        return treeVoList;
    }

    /**
     * 根据权限与权限列表创建TreeVo
     * @param authority
     * @param list
     * @return
     */
    private TreeVo createTreeVo(Authority authority,List<TreeVo> list){
        TreeVo treeVo = new TreeVo();
        treeVo.setId(authority.getAuthorityId());
        treeVo.setTitle(authority.getName());
        treeVo.setChildren(list);
        return treeVo;
    }
}
