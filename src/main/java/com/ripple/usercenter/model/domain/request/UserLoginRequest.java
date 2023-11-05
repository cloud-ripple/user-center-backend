package com.ripple.usercenter.model.domain.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 花海
 * @date 2023/11/5
 * @description 用户登录请求体
 */

@Data
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = 4555994814975673034L;
    private String userAccount;
    private String userPassword;

}
