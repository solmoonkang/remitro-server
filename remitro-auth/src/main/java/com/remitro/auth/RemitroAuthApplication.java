package com.remitro.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.remitro.auth.infrastructure.config.TokenConfig;

@EnableConfigurationProperties(TokenConfig.class)
@SpringBootApplication
public class RemitroAuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(RemitroAuthApplication.class, args);
	}

}
