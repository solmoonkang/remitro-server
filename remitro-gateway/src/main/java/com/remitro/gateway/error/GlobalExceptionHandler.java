package com.remitro.gateway.error;

import java.net.ConnectException;
import java.util.concurrent.TimeoutException;

import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
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
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {

	private final ObjectMapper objectMapper;

	public GlobalExceptionHandler(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@Override
	public Mono<Void> handle(ServerWebExchange serverWebExchange, Throwable throwable) {
		if (serverWebExchange.getResponse().isCommitted()) {
			log.error("[✅ LOGGER] 게이트웨이 응답이 이미 커밋되었습니다. TYPE = {}, MESSAGE = {}",
				throwable.getClass().getName(), throwable.getMessage(), throwable
			);
			return Mono.error(throwable);
		}

		final GatewayErrorCode gatewayErrorCode = resolveErrorCode(throwable);

		logError(serverWebExchange, throwable, gatewayErrorCode);

		ServerHttpResponse serverHttpResponse = serverWebExchange.getResponse();
		serverHttpResponse.setStatusCode(HttpStatus.valueOf(gatewayErrorCode.getStatus()));
		serverHttpResponse.getHeaders().setContentType(MediaType.APPLICATION_JSON);

		return writeErrorResponse(serverHttpResponse, gatewayErrorCode);
	}

	private GatewayErrorCode resolveErrorCode(Throwable throwable) {
		if (throwable == null) {
			return GatewayErrorCode.INTERNAL_ERROR;
		}

		// 커스텀 Gateway 예외
		if (throwable instanceof GatewayException gatewayException) {
			return gatewayException.getGatewayErrorCode();
		}

		// Spring WebFlux 상태 예외
		if (throwable instanceof ResponseStatusException responseStatusException) {
			return resolveByHttpStatus(responseStatusException.getStatusCode());
		}

		// DownStream 통신 오류
		if (isDownstreamException(throwable)) {
			return GatewayErrorCode.DOWNSTREAM_UNAVAILABLE;
		}

		// 메시지 기반 404 추정
		if (throwable.getMessage() != null && throwable.getMessage().contains("404")) {
			return GatewayErrorCode.API_NOT_FOUND;
		}

		return GatewayErrorCode.INTERNAL_ERROR;
	}

	private GatewayErrorCode resolveByHttpStatus(HttpStatusCode httpStatusCode) {
		if (httpStatusCode.equals(HttpStatus.NOT_FOUND)) {
			return GatewayErrorCode.API_NOT_FOUND;
		}

		if (httpStatusCode.equals(HttpStatus.METHOD_NOT_ALLOWED)) {
			return GatewayErrorCode.METHOD_NOT_ALLOWED;
		}

		if (httpStatusCode.equals(HttpStatus.UNAUTHORIZED)) {
			return GatewayErrorCode.UNAUTHORIZED;
		}

		return GatewayErrorCode.INTERNAL_ERROR;
	}

	private boolean isDownstreamException(Throwable throwable) {
		if (throwable == null) {
			return false;
		}

		final boolean isCommunicationError = throwable instanceof WebClientException
			|| throwable instanceof ConnectException
			|| throwable instanceof TimeoutException
			|| throwable.getClass().getName().contains("ConnectException");

		return isCommunicationError || isDownstreamException(throwable.getCause());
	}

	private void logError(ServerWebExchange serverWebExchange, Throwable throwable, GatewayErrorCode gatewayErrorCode) {
		final ServerHttpRequest serverHttpRequest = serverWebExchange.getRequest();

		log.error("[✅ LOGGER] 게이트웨이 오류가 발생했습니다. (CODE = {}, METHOD = {}, PATH = {})",
			gatewayErrorCode.name(),
			serverHttpRequest.getMethod(),
			serverHttpRequest.getURI().getPath()
		);

		log.error("[✅ LOGGER] 게이트웨이 상세 오류: TYPE = {}, MESSAGE = {}",
			throwable.getClass().getName(),
			throwable.getMessage(),
			throwable
		);
	}

	private Mono<Void> writeErrorResponse(ServerHttpResponse serverHttpResponse, GatewayErrorCode gatewayErrorCode) {
		try {
			final byte[] bytes = objectMapper.writeValueAsBytes(ErrorResponse.of(
				gatewayErrorCode.name(),
				gatewayErrorCode.getMessage()
			));

			final DataBuffer dataBuffer = serverHttpResponse.bufferFactory().wrap(bytes);
			return serverHttpResponse.writeWith(Mono.just(dataBuffer));

		} catch (Exception e) {
			log.error("[✅ LOGGER] 게이트웨이 오류로 인해 응답 직렬화에 실패했습니다.", e);
			return Mono.error(e);
		}
	}
}
