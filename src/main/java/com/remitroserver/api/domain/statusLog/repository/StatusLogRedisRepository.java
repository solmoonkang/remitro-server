package com.remitroserver.api.domain.statusLog.repository;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.remitroserver.api.infrastructure.redis.ListRedisRepository;
import com.remitroserver.global.common.util.JsonConverter;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class StatusLogRedisRepository {

	public static final String STATUS_LOG_KEY_PREFIX = "STATUS_LOG_PENDING:";
	public static final Duration STATUS_LOG_EXPIRATION = Duration.ofMinutes(10);

	private final ListRedisRepository listRedisRepository;
	private final JsonConverter jsonConverter;

	public void saveStatusLog(UUID transactionToken, String statusLogJson) {
		final String serializationJsonMessage = jsonConverter.toJson(statusLogJson);
		listRedisRepository.leftPushWithExpire(
			generateStatusLogKey(transactionToken), serializationJsonMessage, STATUS_LOG_EXPIRATION);
	}

	public List<String> getStatusLogs(UUID transactionToken, int count) {
		List<String> statusLogList = new ArrayList<>();

		for (int i = 0; i < count; i++) {
			String statusLog = listRedisRepository.rightPop(generateStatusLogKey(transactionToken));
			if (statusLog == null) break;
			statusLogList.add(statusLog);
		}

		return statusLogList;
	}

	private String generateStatusLogKey(UUID transactionToken) {
		return STATUS_LOG_KEY_PREFIX + transactionToken;
	}
}
