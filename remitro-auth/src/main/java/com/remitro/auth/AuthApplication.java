package com.remitro.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

import com.remitro.auth.infrastructure.config.JwtConfig;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.remitro.auth")
@EnableConfigurationProperties(JwtConfig.class)
public class AuthApplication {
	public static void main(String[] args) {
		SpringApplication.run(AuthApplication.class, args);
	}
}
