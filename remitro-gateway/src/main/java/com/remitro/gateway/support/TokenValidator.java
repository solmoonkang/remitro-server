package com.remitro.gateway.support;

import org.springframework.stereotype.Component;

@Component
public class TokenValidator {

	public boolean isValid(String token) {
		return token != null && !token.isBlank();
	}
}
