package com.remitro.member.infrastructure.config;

import java.util.concurrent.TimeUnit;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.benmanes.caffeine.cache.Caffeine;

@Configuration
@EnableCaching
public class CacheConfig {

	@Bean
	public CacheManager cacheManager() {
		CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager();
		caffeineCacheManager.setCaffeine(objectCaffeine());
		return caffeineCacheManager;
	}

	private Caffeine<Object, Object> objectCaffeine() {
		return Caffeine.newBuilder()
			.expireAfterWrite(30, TimeUnit.MINUTES)
			.maximumSize(10000)
			.recordStats();
	}
}
