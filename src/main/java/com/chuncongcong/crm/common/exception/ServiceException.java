package com.chuncongcong.crm.common.exception;

import lombok.Data;

/**
 * 自定义运行时异常
 * @author HU
 * @date 2022/1/21 10:01
 */

@Data
public class ServiceException extends RuntimeException{

    private Integer code;
    private Integer httpCode;

    public final static Integer DEFAULT_CODE = 0;

    public final static Integer DEFAULT_HTTP_CODE = 200;

    public ServiceException(String message){
        this(DEFAULT_CODE, DEFAULT_HTTP_CODE, message);
    }

    public ServiceException(int code, int httpCode, String message) {
        super(message);
        this.code = code;
        this.httpCode = httpCode;
    }

    public ServiceException(int code, int httpCode, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.httpCode = httpCode;
    }

    public ServiceException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.httpCode = errorCode.getHttpCode();
    }

    public ServiceException(ErrorCode errorCode , String message) {
        super(message);
        this.code = errorCode.getCode();
        this.httpCode = errorCode.getHttpCode();
    }

}
