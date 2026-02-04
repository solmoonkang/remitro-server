package com.remitro.gateway.filter;

import java.util.Optional;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.remitro.gateway.support.TraceIdGenerator;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class GlobalLoggingFilter implements GlobalFilter, Ordered {

	private static final String HEADER_TRACE_ID = "X-Trace-Id";

	private final TraceIdGenerator traceIdGenerator;

	public GlobalLoggingFilter(TraceIdGenerator traceIdGenerator) {
		this.traceIdGenerator = traceIdGenerator;
	}

	@Override
	public Mono<Void> filter(ServerWebExchange serverWebExchange, GatewayFilterChain gatewayFilterChain) {
		final long startTime = System.currentTimeMillis();

		final String traceId = Optional.ofNullable(serverWebExchange.getRequest().getHeaders().getFirst(HEADER_TRACE_ID))
			.orElse(traceIdGenerator.generate());

		ServerHttpRequest decoratedHttpReqeust = new ServerHttpRequestDecorator(serverWebExchange.getRequest()) {

			@Override
			public HttpHeaders getHeaders() {
				HttpHeaders newHttpHeaders = new HttpHeaders();
				newHttpHeaders.putAll(super.getHeaders());
				newHttpHeaders.set(HEADER_TRACE_ID, traceId);
				return newHttpHeaders;
			}
		};

		ServerWebExchange mutatedWebExchange = serverWebExchange.mutate()
			.request(decoratedHttpReqeust)
			.build();

		mutatedWebExchange.getResponse().beforeCommit(() -> {
			mutatedWebExchange.getResponse().getHeaders().set(HEADER_TRACE_ID, traceId);
			return Mono.empty();
		});

		return gatewayFilterChain.filter(mutatedWebExchange)
			.doFinally(signalType -> {
				final long duration = System.currentTimeMillis() - startTime;

				log.info(
					"[✅ LOGGER] 게이트웨이 외부 요청 처리를 완료했습니다. (METHOD = {}, PATH = {}, STATUS = {}, TRACE_ID = {}, DURATION = {})",
					serverWebExchange.getRequest().getMethod(),
					serverWebExchange.getRequest().getURI().getPath(),
					serverWebExchange.getResponse().getStatusCode(),
					traceId,
					duration
				);
			});
	}

	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE + 1;
	}
}
