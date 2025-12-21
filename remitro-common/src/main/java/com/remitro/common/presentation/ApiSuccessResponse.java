package com.remitro.common.presentation;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "API 성공 응답")
public record ApiSuccessResponse(
	@Schema(description = "성공 메시지", example = "요청이 성공적으로 처리되었습니다.")
	String message
) {

	public static ApiSuccessResponse success(String message) {
		return new ApiSuccessResponse(message);
	}
}
