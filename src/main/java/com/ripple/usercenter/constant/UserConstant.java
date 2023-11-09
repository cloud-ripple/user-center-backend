package com.ripple.usercenter.constant;

/**
 * @author 花海
 * @date 2023/11/5
 * @description 用户常量
 */
public interface UserConstant {
    /**
     * 用户登录键
     */
    public static final String USER_LOGIN_STATE = "userLoginState";

    // 用户角色 0-普通用户 1-管理员
    /**
     * 默认权限
     */
    int DEFAULT_ROLE = 0;

    /**
     * 管理员权限
     */
    int ADMIN_ROLE = 1;
}
