package com.ripple.usercenter.model.domain.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 花海
 * @date 2023/11/5
 * @description 用户注册请求体, 前端传递过来的请求参数封装在 UserRegisterRequest 实体类中
 */

@Data
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = -7920460114493865421L;
    private String userAccount; //用户账号
    private String userPassword; //用户密码
    private String checkPassword; //确认密码
    private String planetCode; //星球编号

}
