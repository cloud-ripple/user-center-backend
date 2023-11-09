package com.ripple.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ripple.usercenter.common.BaseResponse;
import com.ripple.usercenter.common.ErrorCode;
import com.ripple.usercenter.common.ResultUtils;
import com.ripple.usercenter.exception.BusinessException;
import com.ripple.usercenter.model.domain.User;
import com.ripple.usercenter.model.domain.request.UserLoginRequest;
import com.ripple.usercenter.model.domain.request.UserRegisterRequest;
import com.ripple.usercenter.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.jws.soap.SOAPBinding;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static com.ripple.usercenter.constant.UserConstant.ADMIN_ROLE;
import static com.ripple.usercenter.constant.UserConstant.USER_LOGIN_STATE;

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
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            return ResultUtils.error(ErrorCode.NULL_ERROR, "");
        }
        log.info("用户注册请求参数，{}", userRegisterRequest);
        // 前端传递过来的所有请求参数封装在 UserRegisterRequest 实体类中
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String planetCode = userRegisterRequest.getPlanetCode();
        // 简单校验，如果这3个请求参数有一个为空
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword, planetCode)) {
            return ResultUtils.error(ErrorCode.NULL_ERROR, "");
        }
        // 调用服务层注册方法
        long result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        return ResultUtils.success(result);
    }

    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            return ResultUtils.error(ErrorCode.NULL_ERROR, "数据为空");
        }
        log.info("用户登录请求参数：{}", userLoginRequest);
        // 前端传递过来的 账户、密码 参数封装在 UserLoginRequest 实体类中，而Http请求对象封装在 HttpServletRequest
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        // 简单校验，如果这2个请求参数有一个为空
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        User user = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(user);
    }

    @PostMapping("/logout")
    public void userLogout(HttpServletRequest request) {
        log.info("退出登录...,{}", request.getSession().getAttribute(USER_LOGIN_STATE));
        if (request == null) {
            return;
        }
        userService.UserLogout(request);
    }


    // 获取当前登录用户
    @GetMapping("/current")
    public User getCurrentUser(HttpServletRequest request) {
        // 从 session 中拿到用户的登录状态信息
        User userObj = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        if (userObj == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN, "用户没有登录");
        }
        long userId = userObj.getId();
        // todo 校验用户是否合法
        // 从数据库中去查询当前登录用户
        User user = userService.getById(userId);
        return userService.getSafetyUser(user); // 返回脱敏后的用户
    }

    // 根据用户名模糊查询
    @GetMapping("/search")
    public List<User> searchUsers(String username, HttpServletRequest request) {
        log.info("查询用户参数 username：{}", username);
        // 仅管理员可查询
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH, "仅管理员可查询"); //如果不是管理员返回空列表
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        // isNotBlank 判断该字符串长度是否为0、为空
        if (StringUtils.isNotBlank((username))) {
            queryWrapper.like("username", username); //模糊查询
        }

        // 把用户敏感信息过滤掉，不要返回给前端
        List<User> userList = userService.list(queryWrapper); //从数据库中查询到的用户
        List<User> safetyUsersList = new ArrayList<>(); // 脱敏处理后的用户

        for (User user : userList) {
            safetyUsersList.add(userService.getSafetyUser(user));
        }
        return safetyUsersList;
    }

    // 根据id删除用户
    @PostMapping("/delete")
    public boolean deleteUser(long id, HttpServletRequest request) {
        log.info("删除用户参数 id：{}", id);
        // 仅管理员可删除
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH, "仅管理员可删除");
        }
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "该删除的用户id不合法");
        }
        return userService.removeById(id);
    }

    /**
     * 是否为管理员
     *
     * @param request 请求对象
     * @return
     */
    private boolean isAdmin(HttpServletRequest request) {
        // 仅管理员可查询
        User userObj = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        if (userObj == null || userObj.getUserRole() != ADMIN_ROLE) {
            throw new BusinessException(ErrorCode.NO_AUTH, "用户未登录或非管理员");
        }
        return true;
    }
}
