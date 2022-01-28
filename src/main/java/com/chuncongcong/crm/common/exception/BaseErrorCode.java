package com.chuncongcong.crm.common.exception;

import lombok.Data;

/**
 * 异常code
 * @author HU
 * @date 2022/1/21 10:01
 */

@Data
public class BaseErrorCode implements ErrorCode {

    private int code;
    private int httpCode;
    private String message;

    public BaseErrorCode(int code, int httpCode, String message) {
        this.httpCode = httpCode;
        this.code = code;
        this.message = message;
    }

    /**
     * 系统异常
     */
    public static final ErrorCode SYSTEM_ERROR = new BaseErrorCode(500, 200, "system error");

    /**
     * 接口为找到
     */
    public static final ErrorCode API_NOT_FOUND = new BaseErrorCode(404, 200, "api not found");

    /**
     * 未登陆
     */
    public static final ErrorCode NOT_LOGIN = new BaseErrorCode(401, 200, "not login");

    /**
     * 没有权限
     */
    public static final ErrorCode NOT_PERMISSION = new BaseErrorCode(403, 200, "权限不足");

    /**
     * 登陆失败
     */
    public static final ErrorCode LOGIN_FAIL = new BaseErrorCode(0, 200, "登陆失败，用户名密码不正确");

}
