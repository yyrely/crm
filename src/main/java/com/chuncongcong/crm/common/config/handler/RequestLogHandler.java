package com.chuncongcong.crm.common.config.handler;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * 日志输出处理器
 * @author HU
 * @date 2022/1/19 14:28
 */

@Slf4j
@Order(1)
@Aspect
@Component
@ConditionalOnProperty(name = "request.log.enable", havingValue = "true", matchIfMissing = true)
public class RequestLogHandler {

    @Autowired
    private ObjectMapper objectMapper;

    @Pointcut("execution(* com.chuncongcong.crm.controller.*.*(..))")
    public void pointcut() {}

    @Around("pointcut()")
    private Object requestLog(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes)ra;
        HttpServletRequest request = sra.getRequest();
        String ipAddr = getRemoteHost(request);
        String url = request.getRequestURL().toString();
        log.info("request start  =================>  [ip:{}],[url:{}]", ipAddr, url);
        Object result = joinPoint.proceed();
        log.info("response end  =================>  [ip:{}],[time:{}],[result:{}]", ipAddr,
            (System.currentTimeMillis() - startTime), objectMapper.writeValueAsString(result));
        return result;

    }

    /**
     * 获取目标主机的ip
     *
     * @param request
     * @return
     */
    private String getRemoteHost(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
    }
}
