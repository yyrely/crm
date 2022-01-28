package com.chuncongcong.crm.common.config.authorization;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.chuncongcong.crm.model.po.sys.SysUserPo;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * 登陆拦截器
 * @author HU
 * @date 2022/1/20 10:47
 */

@Slf4j
public class PostJsonAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final ObjectMapper objectMapper;

    private final AuthenticationManager authenticationManager;

    public PostJsonAuthenticationFilter(AuthenticationManager authenticationManager,
            ObjectMapper objectMapper) {
        super(new AntPathRequestMatcher("/api/user/login", HttpMethod.POST.toString()));
        this.authenticationManager = authenticationManager;
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            // 从body中取出,参数是在请求体中使用json形式传入的
            SysUserPo users = objectMapper.readValue(request.getInputStream(), SysUserPo.class);
            // 模仿UsernamePasswordAuthenticationFilter的方式，将用户名密码进行比较
            return authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(users.getUsername(), users.getPassword()));
        } catch (IOException e) {
            log.error("登陆异常: {}", e.getMessage(), e);
            throw new UsernameNotFoundException(e.getMessage());
        }
    }
}
