package com.chuncongcong.crm.model.vo.sys;

import java.util.List;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author HU
 * @date 2022/1/20 15:20
 */

@Data
public class SysRoleVo {

    @ApiModelProperty(value = "角色id")
    private Long id;

    @NotNull(message = "角色名称不能为空")
    @ApiModelProperty(value = "角色名称")
    private String roleName;

    @NotNull(message = "角色code不能为空")
    @ApiModelProperty(value = "角色code")
    private String roleCode;

    @ApiModelProperty(value = "备注")
    private String roleDesc;

    @ApiModelProperty(value = "类型（0-全部，1-自定义，2-本级及以下，3-本级）")
    private String dsType;

    @ApiModelProperty(value = "自定义的范围")
    private String dsScope;

    @ApiModelProperty(value = "菜单id列表")
    private List<Long> menuIds;
}
