package com.chuncongcong.crm.model.po.sys;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.chuncongcong.crm.model.po.BaseFiled;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author HU
 * @date 2022/1/19 15:43
 */


@Data
@TableName("sys_user")
@EqualsAndHashCode(callSuper = true)
public class SysUserPo extends BaseFiled {

    @TableId
    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "部门id")
    private Long deptId;
}
