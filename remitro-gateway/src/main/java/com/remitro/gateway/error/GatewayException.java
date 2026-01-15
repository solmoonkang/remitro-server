package com.remitro.gateway.error;

import lombok.Getter;

@Getter
public class GatewayException extends RuntimeException {

	private final GatewayErrorCode gatewayErrorCode;

	public GatewayException(GatewayErrorCode gatewayErrorCode) {
		this.gatewayErrorCode = gatewayErrorCode;
	}
}
