package com.remitro.member.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.remitro.support.util.DataHasher;

@Configuration
public class HashingConfig {

	@Value("${security.hash.hmac-secret}")
	private String hmacSecret;

	@Bean
	public DataHasher dataHasher() {
		return new DataHasher(hmacSecret);
	}
}
