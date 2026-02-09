package com.remitro.member.infrastructure.web;

import java.io.IOException;

import org.jboss.logging.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class TraceIdLoggingFilter extends OncePerRequestFilter {

	private static final String HEADER_TRACE_ID = "X-Trace-Id";
	private static final String MDC_TRACE_ID = "traceId";

	@Override
	protected void doFilterInternal(
		HttpServletRequest httpServletRequest,
		HttpServletResponse httpServletResponse,
		FilterChain filterChain
	) throws ServletException, IOException {

		final String traceId = httpServletRequest.getHeader(HEADER_TRACE_ID);
		if (traceId != null && !traceId.isBlank()) {
			MDC.put(MDC_TRACE_ID, traceId);
		}

		try {
			filterChain.doFilter(httpServletRequest, httpServletResponse);

		} finally {
			MDC.remove(MDC_TRACE_ID);
		}
	}
}
