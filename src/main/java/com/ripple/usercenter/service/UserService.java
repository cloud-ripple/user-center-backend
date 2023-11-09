package com.ripple.usercenter.service;

import com.ripple.usercenter.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 花海
 * @description 针对表【user(用户)】的数据库操作Service
 * @createDate 2023-11-03 22:03:34
 */
public interface UserService extends IService<User> {


    /**
     * 用户注册
     *
     * @param userAccount   用户账号
     * @param userPassword  用户密码
     * @param checkPassword 校验密码
     * @return 新用户 id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword, String planetCode);

    /**
     * 用户登录
     *
     * @param userAccount  用户账户
     * @param userPassword 用户密码
     * @param request      请求
     * @return 脱敏后的用户信息
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 用户信息脱敏
     *
     * @param user 数据库中查询到的用户
     * @return 脱敏处理后的用户
     */
    User getSafetyUser(User user);

    /**
     * 用户注销
     *
     * @param request 请求对象
     * @return
     */
    void UserLogout(HttpServletRequest request);
}
