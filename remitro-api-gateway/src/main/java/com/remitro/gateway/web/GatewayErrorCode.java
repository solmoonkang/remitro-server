package com.remitro.gateway.web;

public enum GatewayErrorCode {

	INVALID_TOKEN("INVALID_TOKEN"),
	FORBIDDEN("FORBIDDEN");

	private final String code;

	GatewayErrorCode(String code) {
		this.code = code;
	}

	public String toJson() {
		return "{\"message\":\"" + code + "\"}";
	}
}
