package com.chuncongcong.crm.common.exception;

public interface ErrorCode {
    int getCode();
    String getMessage();
    int getHttpCode();
}
