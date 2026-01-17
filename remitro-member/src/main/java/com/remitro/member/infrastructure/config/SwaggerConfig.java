package com.remitro.member.infrastructure.config;

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
			.title("REMITRO MEMBER API")
			.description("회원 및 인증 관련 API 문서")
			.version("v1.0.0");
	}
}
