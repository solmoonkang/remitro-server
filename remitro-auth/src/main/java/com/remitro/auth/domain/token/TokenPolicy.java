package com.remitro.auth.domain.token;

import java.time.Duration;
import java.time.Instant;

import org.springframework.stereotype.Component;

import com.remitro.auth.domain.model.RefreshToken;
import com.remitro.common.error.code.ErrorCode;
import com.remitro.common.error.exception.UnauthorizedException;
import com.remitro.common.error.message.ErrorMessage;

@Component
public class TokenPolicy {

	public static final Long REFRESH_TOKEN_TTL = Duration.ofDays(7).toMillis();

	public void validateTokenReissuable(RefreshToken refreshToken) {
		if (refreshToken.revoked() || isExpired(refreshToken)) {
			throw new UnauthorizedException(
				ErrorCode.TOKEN_INVALID,
				ErrorMessage.TOKEN_INVALID
			);
		}
	}

	private boolean isExpired(RefreshToken refreshToken) {
		return Instant.now().getEpochSecond() > refreshToken.expirationTime();
	}

	public RefreshToken createRefreshToken(Long memberId, String token, String deviceId) {
		return new RefreshToken(
			memberId,
			token,
			deviceId,
			false,
			Instant.now().plusSeconds(REFRESH_TOKEN_TTL).getEpochSecond()
		);
	}
}
