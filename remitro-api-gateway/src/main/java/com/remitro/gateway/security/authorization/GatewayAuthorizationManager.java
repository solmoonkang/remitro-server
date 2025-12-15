package com.remitro.gateway.security.authorization;


import java.util.List;

import org.springframework.http.HttpMethod;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import com.remitro.common.security.Role;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GatewayAuthorizationManager {

	private final List<AuthorizationPolicy> authorizationPolicies;

	public void authorize(String path, HttpMethod httpMethod, Role role) {
		authorizationPolicies.forEach(
			authorizationPolicy -> validate(authorizationPolicy, path, httpMethod, role)
		);
	}

	private void validate(AuthorizationPolicy authorizationPolicy, String path, HttpMethod httpMethod, Role role) {
		if (authorizationPolicy.supports(path, httpMethod) && !authorizationPolicy.authorize(role)) {
			throw new AccessDeniedException("FORBIDDEN");
		}
	}
}
