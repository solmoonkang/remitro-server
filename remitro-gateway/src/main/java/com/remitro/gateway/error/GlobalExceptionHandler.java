package com.remitro.gateway.error;


import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.remitro.common.response.ErrorResponse;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {

	private final ObjectMapper objectMapper;

	public GlobalExceptionHandler(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@Override
	public Mono<Void> handle(ServerWebExchange serverWebExchange, Throwable throwable) {
		GatewayErrorCode gatewayErrorCode = resolveErrorCode(throwable);

		log.error("[✅ LOGGER] 게이트웨이 오류 발생: {}, MESSAGE = {}", gatewayErrorCode.name(), throwable.getMessage());

		serverWebExchange.getResponse()
			.setStatusCode(HttpStatus.valueOf(gatewayErrorCode.getStatus()));
		serverWebExchange.getResponse()
			.getHeaders()
			.setContentType(MediaType.APPLICATION_JSON);

		return writeErrorResponse(serverWebExchange, gatewayErrorCode);
	}

	private GatewayErrorCode resolveErrorCode(Throwable throwable) {
		if (throwable instanceof GatewayException gatewayException) {
			return gatewayException.getGatewayErrorCode();
		}
		return GatewayErrorCode.INTERNAL_ERROR;
	}

	private Mono<Void> writeErrorResponse(ServerWebExchange serverWebExchange, GatewayErrorCode gatewayErrorCode) {
		try {
			byte[] bytes = objectMapper.writeValueAsBytes(ErrorResponse.of(
				gatewayErrorCode.name(),
				gatewayErrorCode.getMessage()
			));

			DataBuffer dataBuffer = serverWebExchange.getResponse()
				.bufferFactory()
				.wrap(bytes);

			return serverWebExchange.getResponse().writeWith(Mono.just(dataBuffer));

		} catch (Exception e) {
			log.error("[✅ LOGGER] JSON 직렬화에 실패했습니다: ", e);
			return Mono.error(e);
		}
	}
}
