package com.chuncongcong.crm.model.vo.sys;

import java.util.List;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author HU
 * @date 2022/1/21 11:57
 */

@Data
public class SysUserVo {

    @ApiModelProperty(value = "主键")
    private Long id;

    @NotNull(message = "用户名不能为空")
    @ApiModelProperty(value = "用户名")
    private String username;

    @NotNull(message = "密码不能为空")
    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @NotNull(message = "部门不能为空")
    @ApiModelProperty(value = "部门id")
    private Long deptId;

    @ApiModelProperty(value = "角色id（查询使用）")
    private Long roleId;

    @NotNull(message = "角色不能为空")
    @ApiModelProperty(value = "角色id列表")
    private List<Long> roleIds;

    @ApiModelProperty(value = "角色列表")
    private List<SysRoleVo> roles;
}
