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

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.chuncongcong.crm.model.po.sys.SysDeptRelationPo;
import com.chuncongcong.crm.model.po.sys.SysRolePo;
import com.chuncongcong.crm.service.sys.ISysDeptRelationService;
import com.chuncongcong.crm.service.sys.ISysRoleService;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;

/**
 * 默认data scope 判断处理器
 * @author Hu
 * @since 2022-01-21
 */

@Component
public class DefaultDataScopeHandle implements DataScopeHandle {

	@Autowired
	private ISysRoleService roleService;

	@Autowired
	private ISysDeptRelationService sysDeptRelationService;

	/**
	 * 计算用户数据权限
	 * @param roleIds
	 * @param deptId
	 * @param deptScope
	 * @return
	 */
	@Override
	public Boolean calcScope(List<Long> roleIds, Long deptId, List<Long> deptScope) {
		// 当前用户的角色为空
		if (CollectionUtil.isEmpty(roleIds) || Objects.isNull(deptId)) {
			return false;
		}
		SysRolePo role = roleService.listByIds(roleIds).stream()
				.min(Comparator.comparingInt(SysRolePo::getDsType)).orElse(null);
		// 角色有可能已经删除了
		if (role == null) {
			return false;
		}
		Integer dsType = role.getDsType();
		// 查询全部
		if (DataScopeTypeEnum.ALL.getType() == dsType) {
			return true;
		}
		// 自定义
		if (DataScopeTypeEnum.CUSTOM.getType() == dsType && StrUtil.isNotBlank(role.getDsScope())) {
			String dsScope = role.getDsScope();
			deptScope.addAll(
					Arrays.stream(dsScope.split(StrUtil.COMMA)).map(Long::parseLong).collect(Collectors.toList()));
		}
		// 查询本级及其下级
		if (DataScopeTypeEnum.OWN_CHILD_LEVEL.getType() == dsType) {
			List<Long> deptIdList = sysDeptRelationService.getDescendantList(deptId).stream()
					.map(SysDeptRelationPo::getDescendant).collect(Collectors.toList());
			deptScope.addAll(deptIdList);
		}
		// 只查询本级
		if (DataScopeTypeEnum.OWN_LEVEL.getType() == dsType) {
			deptScope.add(deptId);
		}
		return false;
	}

}
