package com.chuncongcong.crm.model.po;

import java.time.LocalDateTime;

import com.chuncongcong.crm.model.annotation.Created;
import com.chuncongcong.crm.model.annotation.Modified;

import lombok.Data;

/**
 * @author HU
 * @date 2019/12/19 19:52
 */

@Data
public abstract class BaseFiled {

	@Created
	private LocalDateTime createTime;

	@Modified
	private LocalDateTime updateTime;
}
