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
 * @since 2022-01-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_dept")
@ApiModel(value="SysDeptPo对象", description="部门表")
public class SysDeptPo extends BaseFiled {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "父id")
    private Long parentId;

    @ApiModelProperty(value = "排序")
    private Integer sort;

}
