package com.chuncongcong.crm.common.config.authorization.handler;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.chuncongcong.crm.common.config.handler.ResultBean;
import com.chuncongcong.crm.common.exception.BaseErrorCode;

import cn.hutool.json.JSONUtil;

/**
 * 登陆失败处理器
 * @author HU
 * @date 2022/1/20 10:52
 */

@Component
public class PostJsonAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException exception) throws IOException {
        response.setStatus(200);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().print(JSONUtil.toJsonStr(new ResultBean<>(BaseErrorCode.LOGIN_FAIL)));
        response.flushBuffer();
    }
}
