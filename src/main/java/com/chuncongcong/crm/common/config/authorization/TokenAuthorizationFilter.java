package com.chuncongcong.crm.common.config.authorization;

import static com.chuncongcong.crm.common.constant.RedisKeyConstants.EXPIRE_TIME_12_HOURS;
import static com.chuncongcong.crm.common.constant.RedisKeyConstants.USER_TOKEN_PRE;
import static com.chuncongcong.crm.common.constant.RedisKeyConstants.USER_USERNAME_PRE;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * 请求认证拦截器
 * @author HU
 * @date 2022/1/20 10:52
 */

@Slf4j
@Component
public class TokenAuthorizationFilter extends OncePerRequestFilter {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        // 从请求头中取出token
        String token = request.getHeader("token");
        if (!StringUtils.isEmpty(token)) {
            // 从redis中获取用户名
            String user = redisTemplate.opsForValue().get(USER_TOKEN_PRE + token);
            AuthUser authUser = objectMapper.readValue(user, AuthUser.class);
            if (SecurityContextHolder.getContext().getAuthentication() == null && Objects.nonNull(authUser)) {
                // 解析并设置认证信息（具体实现不清楚）
                UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(authUser, null, authUser.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);

                // 刷新token
                redisTemplate.expire(USER_USERNAME_PRE + authUser.getUsername(), EXPIRE_TIME_12_HOURS,
                    TimeUnit.SECONDS);
                redisTemplate.expire(USER_TOKEN_PRE + token, EXPIRE_TIME_12_HOURS, TimeUnit.SECONDS);
            }
        }
        chain.doFilter(request, response);
    }
}
