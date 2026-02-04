package com.remitro.support.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JsonSupport {

	public static String toJSON(ObjectMapper objectMapper, Object data) {
		try {
			return objectMapper.writeValueAsString(data);

		} catch (JsonProcessingException e) {
			throw new RuntimeException("[❎ ERROR] 데이터 직렬화에 실패했습니다.", e);
		}
	}

	public static <T> T fromJSON(ObjectMapper objectMapper, String jsonData, Class<T> clazz) {
		try {
			return objectMapper.readValue(jsonData, clazz);

		} catch (JsonProcessingException e) {
			throw new RuntimeException("[❎ ERROR] 데이터 역직렬화에 실패했습니다.", e);
		}
	}
}
