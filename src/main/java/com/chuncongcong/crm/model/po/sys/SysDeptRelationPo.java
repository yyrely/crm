package com.chuncongcong.crm.model.po.sys;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author HU
 * @date 2022/1/21 17:43
 */

@Data
@TableName("sys_dept_relation")
@ApiModel(value="SysDeptRelationPo对象", description="")
public class SysDeptRelationPo {

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "祖先节点")
    private Long ancestor;

    @ApiModelProperty(value = "后代节点")
    private Long descendant;
}
