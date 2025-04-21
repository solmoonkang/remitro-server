package com.remitroserver.global.common.util;

import static com.remitroserver.global.common.util.GlobalConstant.*;

import java.util.Random;

import org.springframework.stereotype.Component;

import com.remitroserver.api.domain.account.model.AccountType;

@Component
public class AccountNumberGenerator {

	private static final String BANK_CODE = "110";

	public String generateAccountNumber(AccountType accountType) {
		String random = String.format("%06d", new Random().nextInt(999999));
		return BANK_CODE + HYPHEN + accountType.getCode() + HYPHEN + random;
	}
}
