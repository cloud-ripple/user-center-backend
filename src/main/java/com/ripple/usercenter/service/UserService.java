package com.ripple.usercenter.service;

import com.ripple.usercenter.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 花海
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2023-11-03 22:03:34
*/
public interface UserService extends IService<User> {

    /**
     * 用户注册
     * @param userAccount 用户账号
     * @param userPassword 用户密码
     * @param checkPassword 校验密码
     * @return 新用户 id
     */
    long userRegister(String userAccount,String userPassword, String checkPassword);
}
