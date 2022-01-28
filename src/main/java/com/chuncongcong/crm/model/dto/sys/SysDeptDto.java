package com.chuncongcong.crm.model.dto.sys;

import com.chuncongcong.crm.model.dto.common.TreeNode;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author HU
 * @date 2022/1/24 14:54
 */

@Data
public class SysDeptDto extends TreeNode {

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "排序")
    private Integer sort;
}
