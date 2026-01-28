package com.remitro.gateway.error;

import java.net.ConnectException;
import java.util.concurrent.TimeoutException;

import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.remitro.support.response.ErrorResponse;

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

		logError(serverWebExchange, throwable, gatewayErrorCode);

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

		if (throwable instanceof ResponseStatusException responseStatusException) {
			return resolveByHttpStatus(responseStatusException.getStatusCode());
		}

		if (isDownstreamException(throwable)) {
			return GatewayErrorCode.DOWNSTREAM_UNAVAILABLE;
		}

		return GatewayErrorCode.INTERNAL_ERROR;
	}

	private GatewayErrorCode resolveByHttpStatus(HttpStatusCode httpStatusCode) {
		int currentHttpStatusValue = httpStatusCode.value();

		if (currentHttpStatusValue == HttpStatus.NOT_FOUND.value()) {
			return GatewayErrorCode.API_NOT_FOUND;
		}

		if (currentHttpStatusValue == HttpStatus.METHOD_NOT_ALLOWED.value()) {
			return GatewayErrorCode.METHOD_NOT_ALLOWED;
		}

		return GatewayErrorCode.INTERNAL_ERROR;
	}

	private boolean isDownstreamException(Throwable throwable) {
		return throwable instanceof WebClientException
			|| throwable instanceof ConnectException
			|| throwable instanceof TimeoutException;
	}

	private void logError(ServerWebExchange serverWebExchange, Throwable throwable, GatewayErrorCode gatewayErrorCode) {
		ServerHttpRequest serverHttpRequest = serverWebExchange.getRequest();

		log.error("[✅ LOGGER] 게이트웨이 오류가 발생했습니다. (CODE = {}, METHOD = {}, PATH = {}, MESSAGE = {})",
			gatewayErrorCode.name(),
			serverHttpRequest.getMethod(),
			serverHttpRequest.getURI().getPath(),
			throwable.getMessage()
		);
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
			log.error("[✅ LOGGER] 게이트웨이 오류로 인해 응답 직렬화에 실패했습니다.", e);
			return Mono.error(e);
		}
	}
}
