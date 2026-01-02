package com.remitro.gateway.error;

import java.nio.charset.StandardCharsets;

import org.apache.http.HttpHeaders;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class GatewayErrorResponseWriter {

	public Mono<Void> unauthorized(ServerWebExchange serverWebExchange) {
		return write(serverWebExchange, HttpStatus.UNAUTHORIZED, GatewayErrorCode.INVALID_TOKEN);
	}

	private Mono<Void> write(ServerWebExchange serverWebExchange, HttpStatus status, GatewayErrorCode errorCode) {
		serverWebExchange.getResponse().setStatusCode(status);
		serverWebExchange.getResponse().getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

		DataBuffer dataBuffer = serverWebExchange.getResponse()
			.bufferFactory()
			.wrap(errorCode.toJson().getBytes(StandardCharsets.UTF_8));

		return serverWebExchange.getResponse().writeWith(Mono.just(dataBuffer));
	}
}
