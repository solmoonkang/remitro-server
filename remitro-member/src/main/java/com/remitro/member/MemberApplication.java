package com.remitro.member;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.remitro.member.infrastructure.security.JwtProperties;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
@EnableConfigurationProperties(JwtProperties.class)
public class MemberApplication {
	public static void main(String[] args) {
		SpringApplication.run(MemberApplication.class, args);
	}
}
