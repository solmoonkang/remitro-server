package com.remitro.account.infrastructure.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class RedissonConfig {

	private final RedisProperties redisProperties;

	@Bean
	public RedissonClient redissonClient() {
		Config config = new Config();
		String redisAddress = String.format("redis://%s:%d",
			redisProperties.getHost(),
			redisProperties.getPort());

		config.useSingleServer().setAddress(redisAddress);
		return Redisson.create(config);
	}
}
