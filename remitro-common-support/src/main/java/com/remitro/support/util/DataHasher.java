package com.remitro.support.util;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.remitro.support.error.ErrorCode;
import com.remitro.support.exception.InternalServerException;

public class DataHasher {

	private static final String HMAC_SHA_256 = "HmacSHA256";
	private static final int HEX_RADIX = 16;
	private static final int BYTE_MASK = 0xff;

	private final SecretKeySpec signingKey;

	public DataHasher(String secretKey) {
		this.signingKey = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), HMAC_SHA_256);
	}

	public String hash(String rawData) {
		try {
			Mac mac = Mac.getInstance(HMAC_SHA_256);
			mac.init(signingKey);

			byte[] hashedBytes = mac.doFinal(
				rawData.getBytes(StandardCharsets.UTF_8)
			);
			return toHexString(hashedBytes);

		} catch (GeneralSecurityException e) {
			throw new InternalServerException(ErrorCode.ALGORITHM_CRYPTO_ERROR);
		}
	}

	private String toHexString(byte[] source) {
		StringBuilder hexBuilder = new StringBuilder(source.length * 2);

		for (byte b : source) {
			String hexPair = Integer.toString(b & BYTE_MASK, HEX_RADIX);
			if (hexPair.length() == 1) {
				hexBuilder.append('0');
			}
			hexBuilder.append(hexPair);
		}

		return hexBuilder.toString();
	}
}
