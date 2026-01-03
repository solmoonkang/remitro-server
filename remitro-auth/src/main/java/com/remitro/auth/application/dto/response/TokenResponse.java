package com.remitro.auth.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(name = "TokenResponse", description = "토큰 응답 DTO")
public record TokenResponse(
	@Schema(name = "액세스 토큰", example = "as1fm281m5ksa18ssf9.....")
	String accessToken,

	@Schema(name = "리프레시 토큰", example = "as1fm281m.jsa18s4sf1.....")
	String refreshToken
) {
}

