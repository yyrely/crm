package com.chuncongcong.crm.common.config.authorization;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.chuncongcong.crm.common.config.data.CustomAuthorityDeserializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 自定义用户
 * 
 * @author HU
 * @date 2022/1/20 10:52
 */

@NoArgsConstructor
public class AuthUser implements UserDetails {

    @Getter
    @Setter
    private Long userId;

    @Getter
    @Setter
    private Long deptId;

    @Getter
    @Setter
    private List<Long> roles;

    @Getter
    @Setter
    private Boolean isAllDeptScope;

    @Getter
    @Setter
    private List<Long> deptScope;

    private String username;

    @JsonIgnore
    private String password;

    @JsonDeserialize(using = CustomAuthorityDeserializer.class)
    private Collection<? extends GrantedAuthority> authorities;

    public AuthUser(Long userId, String username, String password, Long deptId, List<Long> roles, List<Long> deptScope,
        Boolean isAllDeptScope, Collection<? extends GrantedAuthority> authorities) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.userId = userId;
        this.roles = roles;
        this.deptId = deptId;
        this.isAllDeptScope = isAllDeptScope;
        this.deptScope = deptScope;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    /**
     * 账号是否未过期，默认是false，记得要改一下
     */
    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 账号是否未锁定，默认是false，记得也要改一下
     */
    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 账号凭证是否未过期，默认是false，记得还要改一下
     */
    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 这个有点抽象不会翻译，默认也是false，记得改一下
     */
    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }
}
