package com.remitro.account.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI openAPI() {
		return new OpenAPI()
			.info(apiInfo());
	}

	private Info apiInfo() {
		return new Info()
			.title("REMITRO ACCOUNT API")
			.description("계좌 개설, 잔액 관리 관련 금융 거래 API 문서")
			.version("v1.0.0");
	}
}
