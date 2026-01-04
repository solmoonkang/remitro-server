package com.remitro.common.presentation;

public record ApiSuccessResponse(
	String message
) {

	public static ApiSuccessResponse success(String message) {
		return new ApiSuccessResponse(message);
	}
}
