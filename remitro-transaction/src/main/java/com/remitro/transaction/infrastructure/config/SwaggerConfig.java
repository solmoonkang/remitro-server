package com.remitro.transaction.infrastructure.config;

import static com.remitro.common.common.util.TokenConstant.*;
import static io.swagger.v3.oas.models.security.SecurityScheme.In.*;
import static io.swagger.v3.oas.models.security.SecurityScheme.Type.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {

	private static final String OPEN_API_TITLE = "온라인 뱅킹 거래내역 서비스 API";
	private static final String OPEN_API_DESCRIPTION = "거래내역 송금, 입출금 기록 관련 API 문서입니다.";
	private static final String SECURITY_SCHEME_DESCRIPTION = "JWT 인증을 위한 BEARER 토큰입니다.";

	@Bean
	public OpenAPI swaggerOpenAPI() {
		final Info info = new Info().title(OPEN_API_TITLE).description(OPEN_API_DESCRIPTION);
		final SecurityRequirement securityRequirement = new SecurityRequirement().addList(AUTHORIZATION_HEADER);
		final SecurityScheme accessTokenSecurityScheme = toSecurityScheme();
		final Components components = new Components().addSecuritySchemes(BEARER_PREFIX, accessTokenSecurityScheme);
		return new OpenAPI().info(info).addSecurityItem(securityRequirement).components(components);
	}

	private SecurityScheme toSecurityScheme() {
		return new SecurityScheme()
			.name(AUTHORIZATION_HEADER)
			.type(HTTP)
			.scheme(BEARER_PREFIX)
			.in(HEADER)
			.description(SECURITY_SCHEME_DESCRIPTION);
	}
}
