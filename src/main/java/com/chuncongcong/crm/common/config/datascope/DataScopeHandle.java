/*
 *    Copyright (c) 2018-2025, btbc All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: btbc
 */

package com.chuncongcong.crm.common.config.datascope;

import java.util.List;

/**
 * data scope 判断处理器,抽象服务扩展
 *
 * @author Hu
 * @since 2022-01-21
 */
public interface DataScopeHandle {

	/**
	 * 计算用户数据权限
	 * @param roleIds 用户角色
	 * @param deptId 用户当前部门id
	 * @param deptScope 部门ID，如果为空表示没有任何数据权限。
	 * @return 返回true表示无需进行数据过滤处理，返回false表示需要进行数据过滤
	 */
	Boolean calcScope(List<Long> roleIds, Long deptId, List<Long> deptScope);

}
