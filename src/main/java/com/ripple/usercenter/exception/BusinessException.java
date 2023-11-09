package com.ripple.usercenter.exception;

import com.ripple.usercenter.common.ErrorCode;

/**
 * @author 花海
 * @date 2023/11/9
 * @description 自定义业务异常
 */
public class BusinessException extends RuntimeException {

    private final int code;
    private final String description;

    public BusinessException(String message, int code, String description) {
        super(message);
        this.code = code;
        this.description = description;
    }

    public BusinessException(ErrorCode errorCode, String description) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
