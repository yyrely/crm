package com.chuncongcong.crm.model.vo.sys;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author HU
 * @date 2022/1/21 17:51
 */

@Data
public class SysDeptVo {

    @ApiModelProperty(value = "主键")
    private Long id;

    @NotNull(message = "名称不能为空")
    @ApiModelProperty(value = "名称")
    private String name;

    @NotNull(message = "父id不能为空")
    @ApiModelProperty(value = "父id")
    private Long parentId;

    @ApiModelProperty(value = "排序")
    private Integer sort;
}
