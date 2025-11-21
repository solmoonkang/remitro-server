package com.remitro.account.infrastructure.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
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
		config.useSingleServer()
			.setAddress(
				String.format(
					"redis://%s:%d", redisProperties.host(), redisProperties.port()
				)
			);

		return Redisson.create(config);
	}
}
