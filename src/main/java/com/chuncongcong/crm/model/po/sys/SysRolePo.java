package com.chuncongcong.crm.model.po.sys;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
@EqualsAndHashCode(callSuper = false)
@TableName("sys_role")
@ApiModel(value="SysRole对象", description="")
public class SysRolePo extends BaseFiled implements Serializable  {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField
    @ApiModelProperty(value = "角色名称")
    private String roleName;

    @ApiModelProperty(value = "角色code")
    private String roleCode;

    @ApiModelProperty(value = "备注")
    private String roleDesc;

    @ApiModelProperty(value = "类型（0-全部，1-自定义，2-本级及以下，3-本级）")
    private Integer dsType;

    @ApiModelProperty(value = "自定义的范围")
    private String dsScope;
}
