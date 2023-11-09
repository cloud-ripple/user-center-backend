package com.ripple.usercenter.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 花海
 * @date 2023/11/9
 * @description
 */

@Data
public class BaseResponse<T> implements Serializable {

    private int code;

    private T data;

    private String massage;

    private String description;

    public BaseResponse(int code, T data, String massage, String description) {
        this.code = code;
        this.data = data;
        this.massage = massage;
        this.description = description;
    }

    // 成功
    public BaseResponse(int code, T data, String description) {
        this(code, data, "ok", description);
    }

    // 失败
    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(), null, errorCode.getMessage(), errorCode.getDescription());
    }
}
