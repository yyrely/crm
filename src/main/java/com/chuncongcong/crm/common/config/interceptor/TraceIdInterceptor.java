package com.chuncongcong.crm.common.config.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.MDC;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.chuncongcong.crm.common.config.authorization.AuthUser;
import com.chuncongcong.crm.common.config.authorization.SecurityUtil;

import cn.hutool.core.lang.UUID;

/**
 * traceId拦截器
 * @author HU
 * @date 2022/1/19 14:28
 */

@Component
public class TraceIdInterceptor implements HandlerInterceptor {

	public static final String TRACE_ID = "traceId";
	public static final String USER_NAME = "userName";
	public static final String USER_ID = "userId";

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		MDC.put(TRACE_ID, UUID.fastUUID().toString(true));
		try {
			AuthUser user = SecurityUtil.getUser();
			if(user != null) {
				MDC.put(USER_NAME, user.getUsername());
				MDC.put(USER_ID, user.getUserId()+"");
			}
		} catch (Exception e) {

		}
		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		MDC.clear();
	}
}
