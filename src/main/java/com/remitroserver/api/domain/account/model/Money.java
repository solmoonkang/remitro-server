package com.remitroserver.api.domain.account.model;

import static com.remitroserver.global.error.model.ErrorMessage.*;

import java.util.Objects;
import java.util.Optional;

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

	public Money(Long value) {
		this.value = Optional.ofNullable(value)
			.filter(money -> money >= 0)
			.orElseThrow(() -> new BadRequestException(INVALID_MONEY_VALUE_ERROR));
	}

	public Money add(Money target) {
		Objects.requireNonNull(target, "추가할 금액이 NULL일 수 없습니다.");
		return new Money(this.value + target.value);
	}

	public Money subtract(Money target) {
		Objects.requireNonNull(target, "차감할 금액이 NULL일 수 없습니다.");

		return Optional.of(target)
			.filter(money -> this.value >= money.value)
			.map(money -> new Money(this.value - money.value))
			.orElseThrow(() -> new BadRequestException(INSUFFICIENT_BALANCE_ERROR));
	}

	public boolean isGreaterThan(Money target) {
		Objects.requireNonNull(target, "비교 대상 금액이 NULL일 수 없습니다.");
		return this.value > target.value;
	}

	public static Money zero() {
		return new Money(0L);
	}

	public boolean isZero() {
		return this.value == 0;
	}
}
