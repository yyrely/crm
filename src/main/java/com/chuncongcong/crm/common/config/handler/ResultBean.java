package com.chuncongcong.crm.common.config.handler;

import java.io.Serializable;

import com.chuncongcong.crm.common.exception.BaseErrorCode;
import com.chuncongcong.crm.common.exception.ServiceException;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * api通用返回数据
 * @author HU
 * @date 2022/1/19 14:28
 */

@Data
@ApiModel("api通用返回数据")
public class ResultBean<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    public final static int SUCCESS = 1;
    public final static int FAIL = 0;

    @ApiModelProperty("标识代码,1表示成功，非1表示出错")
    private int code;

    @ApiModelProperty("返回对象")
    private T data;

    @ApiModelProperty("异常信息")
    private String msg;


    public ResultBean() {
        super();
        this.code = SUCCESS;
        this.msg = "操作成功";
    }
    public ResultBean(T data) {
        super();
        this.code = SUCCESS;
        this.data = data;
        this.msg = "操作成功";
        if (data instanceof BaseErrorCode) {
            BaseErrorCode baseErrorCode = (BaseErrorCode) data;
            this.code = baseErrorCode.getCode();
            this.msg = baseErrorCode.getMessage();
            this.data = null;
        }
    }

    public ResultBean(Throwable e) {
        super();
        this.msg = e.toString();
        this.code = FAIL ;
        if(e instanceof ServiceException){
            this.msg = e.getMessage();
            this.code = ((ServiceException) e).getCode();
        }
    }


}
