package com.remitro.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
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
		final String traceId = traceIdGenerator.generate();
		final long startTime = System.currentTimeMillis();

		serverWebExchange.getResponse().getHeaders().add(HEADER_TRACE_ID, traceId);

		return gatewayFilterChain.filter(serverWebExchange)
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
		return Ordered.HIGHEST_PRECEDENCE;
	}
}
