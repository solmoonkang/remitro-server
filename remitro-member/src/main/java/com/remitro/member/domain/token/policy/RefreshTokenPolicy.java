package com.remitro.member.domain.token.policy;

import org.springframework.stereotype.Component;

import com.remitro.member.domain.token.model.RefreshToken;

@Component
public class RefreshTokenPolicy {

	public boolean matches(RefreshToken storedToken, String requestToken) {
		return storedToken.getToken().equals(requestToken);
	}
}
