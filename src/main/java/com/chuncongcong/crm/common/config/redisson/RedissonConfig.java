package com.chuncongcong.crm.common.config.redisson;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Redisson配置类
 * @author HU
 * @date 2021/3/5 11:50
 */

@Configuration
public class RedissonConfig {

	@Value("${spring.redis.host}")
	private String host;

	@Value("${spring.redis.port}")
	private String port;

	@Value("${spring.redis.password}")
	private String password;

	@Bean
	public RedissonClient redisClient() {
		Config config = new Config();
		config.useSingleServer().setAddress("redis://" + host + ":" + port).setPassword(password);
		return Redisson.create(config);
	}
}
