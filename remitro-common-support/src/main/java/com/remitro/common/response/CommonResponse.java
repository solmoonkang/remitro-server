package com.remitro.common.response;

public record CommonResponse<T>(
	boolean success,

	T data
) {

	public static <T> CommonResponse<T> successNoContent() {
		return new CommonResponse<>(true, null);
	}

	public static <T> CommonResponse<T> success(T data) {
		return new CommonResponse<>(true, data);
	}
}
