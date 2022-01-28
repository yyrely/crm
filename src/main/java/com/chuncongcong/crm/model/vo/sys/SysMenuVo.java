package com.chuncongcong.crm.model.vo.sys;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author HU
 * @date 2022/1/20 16:31
 */

@Data
public class SysMenuVo {

    @ApiModelProperty(value = "主键")
    private Long id;

    @NotNull(message = "名称不能为空")
    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "权限")
    private String permission;

    @ApiModelProperty(value = "路径")
    private String path;

    @ApiModelProperty(value = "父id")
    private Long parentId;

    @ApiModelProperty(value = "图标")
    private String icon;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "类型，0-左菜单，1-按钮")
    private String type;
}
