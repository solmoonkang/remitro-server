package com.remitro.gateway.security.authorization;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import com.remitro.common.security.Role;

@Component
public class InternalAuthorizationPolicy implements AuthorizationPolicy {

	private static final String INTERNAL_PATH_PREFIX = "/internal";

	@Override
	public boolean supports(String path, HttpMethod httpMethod) {
		return path.startsWith(INTERNAL_PATH_PREFIX);
	}

	@Override
	public boolean authorize(Role role) {
		return role == Role.ADMIN;
	}
}
