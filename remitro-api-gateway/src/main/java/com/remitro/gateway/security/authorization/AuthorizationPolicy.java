package com.remitro.gateway.security.authorization;

import org.springframework.http.HttpMethod;

import com.remitro.common.security.Role;

public interface AuthorizationPolicy {

	boolean supports(String path, HttpMethod httpMethod);

	boolean authorize(Role role);
}
