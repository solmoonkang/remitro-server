package com.remitro.account.infrastructure.persistence;

import java.util.Optional;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import com.remitro.account.domain.account.repository.AccountNumberSequenceRepository;
import com.remitro.support.error.ErrorCode;
import com.remitro.support.exception.InternalServerException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
@RequiredArgsConstructor
public class RedisAccountRepository implements AccountNumberSequenceRepository {

	private static final String ACCOUNT_SEQUENCE_FORMAT = "ACCOUNT_SEQUENCE:%s";

	private final StringRedisTemplate stringRedisTemplate;

	@Override
	public Long nextSequence(String type) {
		try {
			return Optional.ofNullable(stringRedisTemplate.opsForValue().increment(generateKey(type)))
				.orElseThrow(() -> new InternalServerException(ErrorCode.INTERNAL_SERVER_ERROR));

		} catch (Exception e) {
			log.error("[✅ ERROR] REDIS 인프라 장애로 인해 계좌 시퀸스 생성에 실패했습니다. (KEY = {}, MESSAGE = {})",
				generateKey(type), e.getMessage()
			);
			throw new InternalServerException(ErrorCode.INTERNAL_SERVER_ERROR, e);
		}
	}

	private String generateKey(String type) {
		return String.format(ACCOUNT_SEQUENCE_FORMAT, type);
	}
}
