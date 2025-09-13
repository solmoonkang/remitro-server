package com.remitro.member.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

	private static final String OPEN_API_TITLE = "온라인 뱅킹 회원 서비스 API";
	private static final String OPEN_API_DESCRIPTION = "회원가입 및 로그인, 회원 정보 관련 API 문서입니다.";

	@Bean
	public OpenAPI swaggerOpenAPI() {
		final Info info = new Info().title(OPEN_API_TITLE).description(OPEN_API_DESCRIPTION);
		return new OpenAPI().info(info);
	}
}


