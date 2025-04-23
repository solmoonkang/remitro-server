package com.remitroserver.api.domain.account.model;

import static com.remitroserver.global.error.model.ErrorMessage.*;

import java.util.Objects;

import com.remitroserver.global.error.exception.BadRequestException;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Money {

	@Column(name = "amount", nullable = false)
	private Long value;

	private Money(Long value) {
		this.value = value;
	}

	public static Money fromNonNegative(Long rawValue) {
		if (rawValue == null || rawValue < 0) {
			throw new BadRequestException(INVALID_MONEY_VALUE_ERROR);
		}

		return new Money(rawValue);
	}

	public static Money fromPositive(Long rawValue) {
		if (rawValue == null || rawValue <= 0) {
			throw new BadRequestException(INVALID_MONEY_VALUE_ERROR);
		}
		return new Money(rawValue);
	}

	public static Money zero() {
		return fromNonNegative(0L);
	}

	public Money add(Money other) {
		Objects.requireNonNull(other, "추가할 금액이 NULL일 수 없습니다.");
		return new Money(this.value + other.value);
	}

	public Money subtract(Money other) {
		Objects.requireNonNull(other, "차감할 금액이 NULL일 수 없습니다.");

		if (this.value < other.value) {
			throw new BadRequestException(INSUFFICIENT_BALANCE_ERROR);
		}

		return new Money(this.value - other.value);
	}

	public boolean isZero() {
		return this.value == 0;
	}
}
