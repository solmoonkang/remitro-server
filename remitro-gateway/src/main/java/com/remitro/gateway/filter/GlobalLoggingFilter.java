package com.remitro.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.remitro.gateway.support.TraceIdGenerator;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class GlobalLoggingFilter implements GlobalFilter, Ordered {

	private static final String TRACE_ID_HEADER = "X-Trace-Id";

	private final TraceIdGenerator traceIdGenerator;

	public GlobalLoggingFilter(TraceIdGenerator traceIdGenerator) {
		this.traceIdGenerator = traceIdGenerator;
	}

	@Override
	public Mono<Void> filter(ServerWebExchange serverWebExchange, GatewayFilterChain gatewayFilterChain) {
		String traceId = traceIdGenerator.generate();

		ServerHttpRequest serverHttpRequest = serverWebExchange.getRequest().mutate()
			.header(TRACE_ID_HEADER, traceId)
			.build();

		long startTime = System.currentTimeMillis();

		return gatewayFilterChain.filter(serverWebExchange.mutate().request(serverHttpRequest).build())
			.doFinally(signalType -> {
				long duration = System.currentTimeMillis() - startTime;

				log.info(
					"[✅ LOGGER] 게이트웨이 외부 요청 처리 완료: METHOD = {}, PATH = {}, STATUS = {}, TRACE_ID = {}, DURATION = {}",
					serverHttpRequest.getMethod(),
					serverHttpRequest.getURI().getPath(),
					serverWebExchange.getResponse().getStatusCode(),
					traceId,
					duration
				);
			});
	}

	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE;
	}
}
