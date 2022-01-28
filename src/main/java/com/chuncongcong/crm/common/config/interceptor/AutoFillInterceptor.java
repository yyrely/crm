package com.chuncongcong.crm.common.config.interceptor;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Properties;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.ReflectionUtils;

import com.chuncongcong.crm.model.annotation.Created;
import com.chuncongcong.crm.model.annotation.Modified;

import lombok.Data;

/**
 * 创建时间，更新时间自动填充拦截器
 * @author HU
 * @date 2022/1/19 14:28
 */

@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})})
public class AutoFillInterceptor implements Interceptor {

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		Object[] args = invocation.getArgs();
		// 获取信息
		MappedStatement mappedStatement = (MappedStatement) args[0];
		Object arg = args[1];
		// 获取创建时间，修改时间注解字段
		Class<?> clazz = arg.getClass();
		AuditingConfig auditingConfig = new AuditingConfig();
		ReflectionUtils.doWithFields(clazz, field -> {
			if(field.getAnnotation(Created.class) != null) {
				auditingConfig.setCreateField(field);
			} else {
				if(field.getAnnotation(Modified.class) != null) {
					auditingConfig.setModifiedField(field);
				}
			}
		}, field -> field.getAnnotation(Created.class) != null || field.getAnnotation(Modified.class) != null);

		// 根据插入、更新设置属性值
		SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
		if(auditingConfig.getCreateField() != null || auditingConfig.getModifiedField() != null) {
			BeanWrapper beanWrapper = new BeanWrapperImpl(arg);
			if(SqlCommandType.INSERT.equals(sqlCommandType)) {
				if(auditingConfig.getCreateField() != null) {
					beanWrapper.setPropertyValue(auditingConfig.getCreateField().getName(), LocalDateTime.now());
				}
				if(auditingConfig.getModifiedField() != null) {
					beanWrapper.setPropertyValue(auditingConfig.getModifiedField().getName(), LocalDateTime.now());
				}
			} else if (SqlCommandType.UPDATE.equals(sqlCommandType)) {
				if(auditingConfig.getModifiedField() != null) {
					beanWrapper.setPropertyValue(auditingConfig.getModifiedField().getName(), LocalDateTime.now());
				}
			}
		}
		return invocation.proceed();
	}

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties properties) {

	}

	@Data
	private static class AuditingConfig {

		private Field createField;

		private Field modifiedField;
	}
}
