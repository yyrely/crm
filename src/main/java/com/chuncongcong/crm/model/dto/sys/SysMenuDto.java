package com.chuncongcong.crm.model.dto.sys;

import com.chuncongcong.crm.model.dto.common.TreeNode;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author HU
 * @date 2022/1/20 17:52
 */

@Data
public class SysMenuDto extends TreeNode {

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "权限")
    private String permission;

    @ApiModelProperty(value = "路径")
    private String path;

    @ApiModelProperty(value = "图标")
    private String icon;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "类型，0-左菜单，1-按钮")
    private Integer type;
}
