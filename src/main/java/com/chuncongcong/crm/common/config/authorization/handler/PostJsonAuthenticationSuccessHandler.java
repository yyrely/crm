package com.chuncongcong.crm.common.config.authorization.handler;

import static com.chuncongcong.crm.common.constant.RedisKeyConstants.EXPIRE_TIME_12_HOURS;
import static com.chuncongcong.crm.common.constant.RedisKeyConstants.USER_TOKEN_PRE;
import static com.chuncongcong.crm.common.constant.RedisKeyConstants.USER_USERNAME_PRE;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.chuncongcong.crm.common.config.authorization.AuthUser;
import com.chuncongcong.crm.common.config.handler.ResultBean;
import com.chuncongcong.crm.model.vo.sys.LoginUserVo;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn.hutool.core.lang.UUID;

/**
 * 登陆成功处理器
 * @author HU
 * @date 2022/1/20 10:59
 */

@Component
public class PostJsonAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException {
        Object principal = authentication.getPrincipal();
        if (principal instanceof AuthUser) {
            AuthUser user = (AuthUser)authentication.getPrincipal();
            String uuid = redisTemplate.opsForValue().get(USER_USERNAME_PRE + user.getUsername());
            if (StringUtils.isEmpty(uuid)) {
                uuid = UUID.fastUUID().toString(true);
            }
            redisTemplate.opsForValue().set(USER_USERNAME_PRE + user.getUsername(), uuid, EXPIRE_TIME_12_HOURS,
                    TimeUnit.SECONDS);
            redisTemplate.opsForValue().set(USER_TOKEN_PRE + uuid, objectMapper.writeValueAsString(user), EXPIRE_TIME_12_HOURS,
                    TimeUnit.SECONDS);

            response.setStatus(200);
            response.setContentType("application/json;charset=UTF-8");
            LoginUserVo loginUserVo = new LoginUserVo();
            loginUserVo.setUserId(user.getUserId());
            loginUserVo.setUsername(user.getUsername());
            loginUserVo.setToken(uuid);
            loginUserVo.setRoles(user.getRoles());
            loginUserVo.setPermissions(AuthorityUtils.authorityListToSet(authentication.getAuthorities()));

            response.getWriter().print(objectMapper.writeValueAsString(new ResultBean<>(loginUserVo)));
            response.flushBuffer();
        }
    }
}
