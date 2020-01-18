package com.wfmyzyz.user.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wfmyzyz.user.config.ProjectConfig;
import com.wfmyzyz.user.user.domain.User;
import com.wfmyzyz.user.user.domain.UserRole;
import com.wfmyzyz.user.user.mapper.UserMapper;
import com.wfmyzyz.user.user.service.IUserRoleService;
import com.wfmyzyz.user.user.service.IUserService;
import com.wfmyzyz.user.user.vo.AddUserVo;
import com.wfmyzyz.user.utils.Md5Utils;
import com.wfmyzyz.user.utils.RandomUtils;
import com.wfmyzyz.user.utils.TokenUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author auto
 * @since 2019-12-19
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private TokenUtils tokenUtils;
    @Autowired
    private IUserRoleService userRoleService;

    private final static Logger logger = LoggerFactory.getLogger("UserServiceImpl");

    @Override
    public boolean addUser(AddUserVo userVo) {
        User user = new User();
        user.setUsername(userVo.getUsername());
        String userSalt = getUserSalt();
        user.setSalt(userSalt);
        String md5PasswordAndSaltStr = getStrByPasswordAndSalt(userVo.getPassword(),userSalt);
        user.setPassword(md5PasswordAndSaltStr);
        return this.save(user);
    }

    @Override
    public boolean updateUserPassword(User user,String newPassword, Integer userId) {
        String userSalt = getUserSalt();
        user.setSalt(userSalt);
        String md5NewPasswordSaltStr = getStrByPasswordAndSalt(newPassword,userSalt);
        user.setPassword(md5NewPasswordSaltStr);
        return this.updateById(user);
    }

    @Override
    public User getUserByUsername(String username) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("username",username);
        return this.getOne(userQueryWrapper);
    }

    @Override
    public User checkUser(String username, String password) {
        User user = this.getUserByUsername(username);
        if (user == null){
            return null;
        }
        String strByPasswordAndSalt = getStrByPasswordAndSalt(password, user.getSalt());
        if (!Objects.equals(strByPasswordAndSalt,user.getPassword())){
            return null;
        }
        return user;
    }

    @Override
    public boolean updateUserStatus(Integer userId,String status) {
        UpdateWrapper<User> updateWrapper = new UpdateWrapper();
        updateWrapper.set("status",status);
        updateWrapper.eq("user_id",userId);
        return this.update(updateWrapper);
    }

    @Override
    public boolean checkAuthority(HttpServletRequest request, Integer userId) {
        List<Integer> canOperationRoleIdNoOnSelf = userRoleService.getCanOperationRoleIdNoOnSelf(request);
        if (canOperationRoleIdNoOnSelf == null || canOperationRoleIdNoOnSelf.size() <= 0){
            return false;
        }
        List<UserRole> userRoleListByUserId = userRoleService.getUserRoleListByUserId(userId);
        if (userRoleListByUserId == null || userRoleListByUserId.size() <= 0){
            return true;
        }
        for (UserRole userRole : userRoleListByUserId) {
            if (!canOperationRoleIdNoOnSelf.contains(userRole.getRoleId())){
                return false;
            }
        }
        return true;
    }

    @Override
    public IPage<User> listByPage(Integer page, Integer limit, Integer userId, String username, String status, String startTime, String endTime) {
        QueryWrapper<User> queryWrapper = new QueryWrapper();
        if (userId != null){
            queryWrapper.eq("user_id",userId);
        }
        if (StringUtils.isNotBlank(username)){
            queryWrapper.like("username",username);
        }
        if (StringUtils.isNotBlank(status)){
            queryWrapper.eq("status", status);
        }
        if (StringUtils.isNotBlank(startTime)){
            queryWrapper.ge("create_time",startTime);
        }
        if (StringUtils.isNotBlank(endTime)){
            queryWrapper.le("create_time",endTime);
        }
        queryWrapper.orderByDesc("create_time");
        return this.page(new Page<>(page,limit),queryWrapper);
    }

    /**
     * 获取用户盐值
     * @return
     */
    private String getUserSalt(){
        return RandomUtils.getRandomStr(ProjectConfig.USER_PASSWORD_START_LEN,ProjectConfig.USER_PASSWORD_END_LEN);
    }

    @Override
    public String getStrByPasswordAndSalt(String password,String salt){
        return Md5Utils.getMd5Str(password+salt);
    }
}
