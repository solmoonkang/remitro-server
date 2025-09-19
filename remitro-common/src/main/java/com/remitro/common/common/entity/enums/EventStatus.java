package com.remitro.common.common.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EventStatus {

	PUBLISHED("발행"),
	PENDING("미발행");

	private final String description;
}
