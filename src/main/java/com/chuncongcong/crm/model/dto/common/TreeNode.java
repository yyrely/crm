package com.chuncongcong.crm.model.dto.common;

import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author HU
 * @date 2022/1/24 14:54
 */

@Data
public class TreeNode {

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "父id")
    private Long parentId;

    @ApiModelProperty(value = "子集合")
    private List<TreeNode> children = new ArrayList<>();
}
