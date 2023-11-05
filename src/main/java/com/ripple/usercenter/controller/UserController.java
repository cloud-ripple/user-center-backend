package com.ripple.usercenter.controller;

import com.ripple.usercenter.model.domain.User;
import com.ripple.usercenter.model.domain.request.UserLoginRequest;
import com.ripple.usercenter.model.domain.request.UserRegisterRequest;
import com.ripple.usercenter.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author 花海
 * @date 2023/11/5
 * @description 用户接口
 */

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/register")
    public Long userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            return null;
        }
        log.info("用户注册请求参数，{}", userRegisterRequest);
        // 前端传递过来的所有请求参数封装在 UserRegisterRequest 实体类中
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        // 简单校验，如果这3个请求参数有一个为空
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return null;
        }
        // 调用服务层注册方法
        return userService.userRegister(userAccount, userPassword, checkPassword);
    }

    @PostMapping("/login")
    public User userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            return null;
        }
        log.info("用户登录请求参数：{}", userLoginRequest);
        // 前端传递过来的 账户、密码 参数封装在 UserLoginRequest 实体类中，而Http请求对象封装在 HttpServletRequest
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        // 简单校验，如果这2个请求参数有一个为空
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return null;
        }
        return userService.userLogin(userAccount, userPassword, request);
    }

}
