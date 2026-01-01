package com.remitro.account;

import org.redisson.spring.starter.RedissonAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.remitro.account.infrastructure.config.RedisProperties;

@SpringBootApplication(
	exclude = {
		RedissonAutoConfiguration.class
	},
	scanBasePackages = {
		"com.remitro.account",
		"com.remitro.common",
		"com.remitro.event"
	}
)
@EnableJpaAuditing
@EnableConfigurationProperties(RedisProperties.class)
public class AccountServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccountServiceApplication.class, args);
	}

}
