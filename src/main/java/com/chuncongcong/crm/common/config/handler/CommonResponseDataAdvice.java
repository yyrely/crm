package com.chuncongcong.crm.common.config.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;

/**
 * 返回对象全局处理器
 * @author HU
 * @date 2022/1/19 14:28
 */

@RestControllerAdvice
public class CommonResponseDataAdvice implements ResponseBodyAdvice<Object> {

    private static final String PACKAGE_NAME = "com.chuncongcong.crm.controller";

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        return filter(methodParameter);
    }

    @SneakyThrows
    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType,
        Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest,
        ServerHttpResponse serverHttpResponse) {

        // o is null -> return response
        if (o == null) {
            return new ResultBean<>();
        }
        // o is instanceof ConmmonResponse -> return o
        if (o instanceof ResultBean) {
            return o;
        }
        // string 特殊处理
        if (o instanceof String) {
            return objectMapper.writeValueAsString(new ResultBean<>(o));
        }
        // 不需要封装，直接原始数据返回
        if (o instanceof ResponseEntity) {
            return o;
        }
        return new ResultBean<>(o);
    }

    private Boolean filter(MethodParameter methodParameter) {
        return methodParameter.getDeclaringClass().getPackage().getName().contains(PACKAGE_NAME);
    }

}
