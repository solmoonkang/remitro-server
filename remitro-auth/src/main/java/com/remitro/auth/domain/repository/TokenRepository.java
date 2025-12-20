package com.remitro.auth.domain.repository;

import static com.remitro.auth.infrastructure.redis.RedisKeys.*;

import java.time.Duration;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.remitro.auth.domain.model.RefreshToken;
import com.remitro.auth.infrastructure.support.JsonMapper;
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
		RefreshToken token = JsonMapper.fromJSON(
			valueRedisRepository.get(refreshTokenKey),
			RefreshToken.class
		);

		if (token.memberId().equals(memberId) && token.deviceId().equals(deviceId)) {
			valueRedisRepository.delete(refreshTokenKey);
		}
	}

	public void revokeAllByMember(Long memberId) {
		valueRedisRepository.scanKeys(REFRESH_TOKEN_PREFIX + "*")
			.forEach(refreshToken -> revokeIfMatchesMember(memberId, refreshToken));
	}

	private void revokeIfMatchesMember(Long memberId, String refreshToken) {
		RefreshToken token = JsonMapper.fromJSON(
			valueRedisRepository.get(generateRefreshTokenKey(refreshToken)),
			RefreshToken.class
		);

		if (token.memberId().equals(memberId)) {
			valueRedisRepository.delete(generateRefreshTokenKey(refreshToken));
		}
	}

	private String generateRefreshTokenKey(String refreshToken) {
		return REFRESH_TOKEN_PREFIX + refreshToken;
	}
}
