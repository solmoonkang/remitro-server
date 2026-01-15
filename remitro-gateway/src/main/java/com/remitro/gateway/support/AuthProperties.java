package com.remitro.gateway.support;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.app.auth")
public record AuthProperties(
	List<String> whitelist
) {
}
