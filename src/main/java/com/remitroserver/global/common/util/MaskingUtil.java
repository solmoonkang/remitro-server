package com.remitroserver.global.common.util;

import static com.remitroserver.global.common.util.GlobalConstant.*;
import static com.remitroserver.global.error.model.ErrorMessage.*;

import com.remitroserver.global.error.exception.BadRequestException;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MaskingUtil {

	private static final String MASK = "******";

	public static String maskRegistrationNumber(String registrationNumber) {
		if (registrationNumber == null || !registrationNumber.contains(HYPHEN)) {
			throw new BadRequestException(INVALID_REGISTRATION_NUMBER_FORMAT_ERROR);
		}

		String[] registrationNumberParts = registrationNumber.split(HYPHEN);
		return registrationNumberParts[0] + HYPHEN + MASK;
	}
}
