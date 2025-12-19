package com.remitro.auth.domain.repository;

import static com.remitro.auth.infrastructure.constant.RedisConstant.*;

import java.time.Duration;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.remitro.auth.domain.model.RefreshToken;
import com.remitro.auth.infrastructure.constant.JsonMapper;
import com.remitro.auth.infrastructure.redis.ValueRedisRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TokenRepository {

	private final ValueRedisRepository valueRedisRepository;

	public void save(RefreshToken refreshToken) {
		valueRedisRepository.set(
			generateRefreshTokenKey(refreshToken.token()),
			JsonMapper.toJSON(refreshToken),
			Duration.ofMillis(refreshToken.expirationTime())
		);
	}

	public Optional<RefreshToken> findByToken(String refreshToken) {
		String token = valueRedisRepository.get(generateRefreshTokenKey(refreshToken));
		return Optional.of(JsonMapper.fromJSON(token, RefreshToken.class));
	}

	public void revoke(String refreshToken) {
		valueRedisRepository.delete(generateRefreshTokenKey(refreshToken));
	}

	public void revokeByMemberAndDevice(Long memberId, String deviceId) {
		valueRedisRepository.scanKeys(REFRESH_TOKEN_PREFIX + "*").forEach(
			refreshTokenKey -> revokeIfMatchesMemberAndDevice(memberId, deviceId, refreshTokenKey)
		);
	}

	private void revokeIfMatchesMemberAndDevice(Long memberId, String deviceId, String refreshTokenKey) {
		RefreshToken refreshToken = JsonMapper.fromJSON(
			valueRedisRepository.get(refreshTokenKey),
			RefreshToken.class
		);

		if (refreshToken.memberId().equals(memberId) && refreshToken.deviceId().equals(deviceId)) {
			valueRedisRepository.delete(refreshTokenKey);
		}
	}

	private String generateRefreshTokenKey(String refreshToken) {
		return REFRESH_TOKEN_PREFIX + refreshToken;
	}
}
