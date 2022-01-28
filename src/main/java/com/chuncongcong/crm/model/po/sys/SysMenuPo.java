package com.chuncongcong.crm.model.po.sys;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.chuncongcong.crm.model.po.BaseFiled;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Hu
 * @since 2022-01-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_menu")
@ApiModel(value="SysMenuPo对象", description="")
public class SysMenuPo extends BaseFiled {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

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
    private Integer type;
}
