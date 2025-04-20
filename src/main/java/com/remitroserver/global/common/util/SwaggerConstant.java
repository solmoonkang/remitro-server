package com.remitroserver.global.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SwaggerConstant {

	public static final String API_TITLE = "Remitro-Server API Docs";
	public static final String API_DESCRIPTION = "사용자 간 간편 송금을 안전하게 처리하기 위한 API 문서입니다.";
	public static final String API_VERSION = "1.0.0";

	public static final String SECURITY_SCHEME_NAME = "AccessToken";
	public static final String SECURITY_SCHEME_DESCRIPTION = "JWT 형식의 Bearer 토큰을 입력하세요";
	public static final String AUTH_HEADER_NAME = "Authorization";
	public static final String BEARER_TYPE = "bearer";
}
