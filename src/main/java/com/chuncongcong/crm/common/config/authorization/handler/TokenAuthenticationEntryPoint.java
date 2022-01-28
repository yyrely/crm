package com.chuncongcong.crm.common.config.authorization.handler;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.chuncongcong.crm.common.config.handler.ResultBean;
import com.chuncongcong.crm.common.exception.BaseErrorCode;

import cn.hutool.json.JSONUtil;

/**
 * 未登录处理器
 * @author HU
 * @date 2022/1/20 10:52
 */

@Component
public class TokenAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException authException) throws IOException {
        response.setStatus(200);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().print(JSONUtil.toJsonStr(new ResultBean<>(BaseErrorCode.NOT_LOGIN)));
        response.flushBuffer();
    }
}
