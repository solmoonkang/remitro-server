package com.remitroserver.api.domain.member.repository;

import static com.remitroserver.global.common.util.JwtConstant.*;
import static com.remitroserver.global.common.util.RedisConstant.*;

import org.springframework.stereotype.Repository;

import com.remitroserver.api.infrastructure.redis.ValueRedisRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TokenRepository {

	private final ValueRedisRepository valueRedisRepository;

	public void saveToken(String email, String refreshToken) {
		valueRedisRepository.save(generateTokenKey(email), refreshToken, REFRESH_TOKEN_EXPIRATION);
	}

	public String findTokenByEmail(String email) {
		return valueRedisRepository.find(generateTokenKey(email));
	}

	public void deleteTokenByEmail(String email) {
		valueRedisRepository.delete(generateTokenKey(email));
	}

	private String generateTokenKey(String email) {
		return REFRESH_TOKEN_KEY_PREFIX + email;
	}
}
