package com.remitro.web.config;

import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.remitro.web.filter.AuthenticatedUserFilter;
import com.remitro.web.filter.TraceIdLoggingFilter;
import com.remitro.web.security.CurrentUserArgumentResolver;
import com.remitro.web.util.CookieManager;
import com.remitro.web.util.idempotency.IdempotencyProvider;
import com.remitro.web.util.idempotency.IdempotencyStorage;
import com.remitro.web.util.NetworkExtractor;

@Configuration
public class CommonWebConfig implements WebMvcConfigurer {

	@Bean
	public CookieManager cookieManager() {
		return new CookieManager();
	}

	@Bean
	public NetworkExtractor networkExtractor() {
		return new NetworkExtractor();
	}

	@Bean
	public CurrentUserArgumentResolver currentUserArgumentResolver() {
		return new CurrentUserArgumentResolver();
	}

	@Bean
	public IdempotencyProvider idempotencyProvider(IdempotencyStorage idempotencyStorage) {
		return new IdempotencyProvider(idempotencyStorage);
	}

	@Bean
	public TraceIdLoggingFilter traceIdLoggingFilter() {
		return new TraceIdLoggingFilter();
	}

	@Bean
	public FilterRegistrationBean<TraceIdLoggingFilter> traceIdLoggingFilterRegistration(
		TraceIdLoggingFilter traceIdLoggingFilter
	) {
		FilterRegistrationBean<TraceIdLoggingFilter> filterRegistrationBean =
			new FilterRegistrationBean<>(traceIdLoggingFilter);

		filterRegistrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		filterRegistrationBean.addUrlPatterns("/*");

		return filterRegistrationBean;
	}

	@Bean
	@ConditionalOnProperty(name = "auth.type", havingValue = "gateway")
	public AuthenticatedUserFilter authenticatedUserFilter() {
		return new AuthenticatedUserFilter();
	}

	@Bean
	@ConditionalOnProperty(name = "auth.type", havingValue = "gateway")
	public FilterRegistrationBean<AuthenticatedUserFilter> authenticatedUserFilterRegistration(
		AuthenticatedUserFilter authenticatedUserFilter
	) {
		FilterRegistrationBean<AuthenticatedUserFilter> filterRegistrationBean =
			new FilterRegistrationBean<>(authenticatedUserFilter);

		filterRegistrationBean.setEnabled(false);

		return filterRegistrationBean;
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> handlerMethodArgumentResolvers) {
		handlerMethodArgumentResolvers.add(currentUserArgumentResolver());
	}
}
