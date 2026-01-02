package com.remitro.gateway.error;

public enum GatewayErrorCode {

	INVALID_TOKEN("INVALID_TOKEN");

	private final String code;

	GatewayErrorCode(String code) {
		this.code = code;
	}

	public String toJson() {
		return "{\"message\":\"" + code + "\"}";
	}
}
