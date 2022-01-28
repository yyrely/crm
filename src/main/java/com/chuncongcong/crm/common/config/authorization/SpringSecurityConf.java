package com.chuncongcong.crm.common.config.authorization;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.chuncongcong.crm.common.config.authorization.handler.PostJsonAuthenticationFailureHandler;
import com.chuncongcong.crm.common.config.authorization.handler.PostJsonAuthenticationSuccessHandler;
import com.chuncongcong.crm.common.config.authorization.handler.TokenAccessDeniedHandler;
import com.chuncongcong.crm.common.config.authorization.handler.TokenAuthenticationEntryPoint;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 权限配置类
 * @author HU
 * @date 2022/1/20 10:52
 */

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class SpringSecurityConf extends WebSecurityConfigurerAdapter {

    private static final String[] AUTH_WHITELIST =
        {"/swagger-resources/**", "/swagger-ui/**", "/swagger-ui.html",
                "/doc.html" ,"/v3/api-docs", "/webjars/**", "/api/user/login"};

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 权限鉴定过滤器
     */
    @Autowired
    private TokenAuthorizationFilter authorizationFilter;

    /**
     * 未登录结果处理
     */
    @Autowired
    private TokenAuthenticationEntryPoint authenticationEntryPoint;

    /**
     * 权限不足结果处理
     */
    @Autowired
    private TokenAccessDeniedHandler accessDeniedHandler;

    /**
     * 登陆成功处理
     */
    @Autowired
    private PostJsonAuthenticationSuccessHandler postJsonAuthenticationSuccessHandler;

    /**
     * 登陆失败处理
     */
    @Autowired
    private PostJsonAuthenticationFailureHandler postJsonAuthenticationFailureHandler;

    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.exceptionHandling()
            .authenticationEntryPoint(authenticationEntryPoint)
            .accessDeniedHandler(accessDeniedHandler);
        http.csrf().disable().httpBasic()
            .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and().authorizeRequests().antMatchers(AUTH_WHITELIST).permitAll()
            .and().authorizeRequests().antMatchers(getAnonymousUrls()).permitAll()
            .anyRequest().authenticated();

        http.addFilterBefore(createTokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(authorizationFilter, UsernamePasswordAuthenticationFilter.class);

    }

    /**
     * 短信登陆过滤器
     */
    private PostJsonAuthenticationFilter createTokenAuthenticationFilter() throws Exception {
        PostJsonAuthenticationFilter postJsonAuthenticationFilter = new PostJsonAuthenticationFilter(authenticationManagerBean(), objectMapper);
        postJsonAuthenticationFilter.setAuthenticationSuccessHandler(postJsonAuthenticationSuccessHandler);
        postJsonAuthenticationFilter.setAuthenticationFailureHandler(postJsonAuthenticationFailureHandler);
        return postJsonAuthenticationFilter;
    }

    /**
     * 密码加密器，在授权时，框架为我们解析用户名密码时，密码会通过加密器加密在进行比较 将密码加密器交给spring管理，在注册时，密码也是需要加密的，再存入数据库中 用户输入登录的密码用加密器加密，再与数据库中查询到的用户密码比较
     * 
     * @return BCryptPasswordEncoder 加密器
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public static void main(String[] args) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encode = bCryptPasswordEncoder.encode("123456");
        System.out.println(encode);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
            // 设置使用自己实现的userDetailsService（loadUserByUsername）
            .userDetailsService(userDetailsService)
            // 设置密码加密方式
            .passwordEncoder(bCryptPasswordEncoder());
    }

    /**
     * 获取标有注解 AnonymousAccess 的访问路径
     */
    private String[] getAnonymousUrls() {
        // 获取所有的 RequestMapping
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods();
        Set<String> allAnonymousAccess = new HashSet<>();
        // 循环 RequestMapping
        for (Map.Entry<RequestMappingInfo, HandlerMethod> infoEntry : handlerMethods.entrySet()) {
            HandlerMethod value = infoEntry.getValue();
            // 获取方法上 AnonymousAccess 类型的注解
            AnonymousAccess methodAnnotation = value.getMethodAnnotation(AnonymousAccess.class);
            // 如果方法上标注了 AnonymousAccess 注解，就获取该方法的访问全路径
            if (methodAnnotation != null) {
                allAnonymousAccess.addAll(infoEntry.getKey().getPatternsCondition().getPatterns());
            }
        }
        return allAnonymousAccess.toArray(new String[0]);
    }
}
