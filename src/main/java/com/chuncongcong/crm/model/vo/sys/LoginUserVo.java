package com.chuncongcong.crm.model.vo.sys;

import java.util.List;
import java.util.Set;

import lombok.Data;

/**
 * @author HU
 * @date 2022/1/20 11:03
 */

@Data
public class LoginUserVo {

    private Long userId;

    private String username;

    private String token;

    private Set<String> permissions;

    private List<Long> roles;
}
