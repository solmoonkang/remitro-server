package com.remitro.account;

import org.redisson.spring.starter.RedissonAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication(
	exclude = {RedissonAutoConfiguration.class}
)
@EnableJpaAuditing
@ComponentScan(
	basePackages = {"com.remitro.account", "com.remitro.member", "com.remitro.common"}
)
public class AccountServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccountServiceApplication.class, args);
	}

}
