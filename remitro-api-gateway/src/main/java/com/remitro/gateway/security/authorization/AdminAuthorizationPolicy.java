package com.remitro.gateway.security.authorization;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import com.remitro.common.security.Role;

@Component
public class AdminAuthorizationPolicy implements AuthorizationPolicy {

	private static final String ADMIN_PATH_PREFIX = "/admin";

	@Override
	public boolean supports(String path, HttpMethod httpMethod) {
		return path.startsWith(ADMIN_PATH_PREFIX) && (httpMethod == HttpMethod.GET || httpMethod == HttpMethod.POST);
	}

	@Override
	public boolean authorize(Role role) {
		return role == Role.ADMIN;
	}
}
