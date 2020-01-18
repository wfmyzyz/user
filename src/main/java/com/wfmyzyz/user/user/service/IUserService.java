package com.wfmyzyz.user.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wfmyzyz.user.user.domain.User;
import com.wfmyzyz.user.user.vo.AddUserVo;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author auto
 * @since 2019-12-19
 */
public interface IUserService extends IService<User> {

    /**
     * 添加用户
     * @param userVo
     * @return
     */
    boolean addUser(AddUserVo userVo);

    /**
     * 根据用户ID与旧密码修改用户密码
     * @param user
     * @param newPassword
     * @param userId
     * @return
     */
    boolean updateUserPassword(User user,String newPassword, Integer userId);

    /**
     * 根据用户名获取用户
     * @param username
     * @return
     */
    User getUserByUsername(String username);

    /**
     * 登录检查用户是否正确
     * @param username
     * @param password
     * @return
     */
    User checkUser(String username,String password);

    /**
     * 根据用户ID改变用户状态
     * @param userId
     * @param status
     * @return
     */
    boolean updateUserStatus(Integer userId,String status);

    /**
     * 根据当前操作员request与被操作员userId判断是否有权限
     * @param request
     * @param userId
     * @return
     */
    boolean checkAuthority(HttpServletRequest request, Integer userId);

    /**
     * 根据条件分页查询用户
     * @param page
     * @param limit
     * @param userId
     * @param username
     * @param status
     * @param startTime
     * @param endTime
     * @return
     */
    IPage<User> listByPage(Integer page, Integer limit, Integer userId, String username, String status, String startTime, String endTime);

    /**
     * 根据密码与盐值得到MD5加密字符串
     * @param password
     * @param salt
     * @return
     */
    String getStrByPasswordAndSalt(String password,String salt);

}
