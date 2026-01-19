package com.remitro.member.domain.token.policy;

import com.remitro.common.error.ErrorCode;
import com.remitro.common.exception.UnauthorizedException;
import com.remitro.member.domain.token.model.RefreshToken;

public class RefreshTokenPolicy {

	public void validateMatch(RefreshToken storedToken, String requestToken) {
		if (!storedToken.getToken().equals(requestToken)) {
			throw new UnauthorizedException(ErrorCode.INVALID_TOKEN, "인증 정보");
		}
	}
}
