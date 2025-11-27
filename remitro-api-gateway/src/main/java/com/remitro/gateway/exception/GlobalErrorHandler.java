package com.remitro.gateway.exception;

import static com.remitro.gateway.constant.GlobalConstant.*;

import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
@Order(-2)
public class GlobalErrorHandler implements ErrorWebExceptionHandler {

	@Override
	public Mono<Void> handle(ServerWebExchange serverWebExchange, Throwable throwable) {
		serverWebExchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
		serverWebExchange.getResponse().getHeaders().add("Content-Type", CONTENT_TYPE_JSON);

		DataBuffer dataBuffer = serverWebExchange.getResponse().bufferFactory().wrap(
			ERROR_INVALID_TOKEN_JSON.getBytes()
		);

		return serverWebExchange.getResponse().writeWith(Mono.just(dataBuffer));
	}
}
