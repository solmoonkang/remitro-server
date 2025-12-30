package com.remitro.account.application.usecase.open.dto.request;

import com.remitro.account.domain.product.enums.ProductType;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
@Schema(name = "OpenDepositRequest", description = "입출금 계좌 개설 요청 DTO")
public record OpenDepositRequest(
	@NotBlank(message = "계좌 이름을 입력해주세요.")
	@Schema(description = "계좌 이름", example = "accountName")
	String accountName,

	@NotBlank(message = "계좌 비밀번호를 입력해주세요.")
	@Size(max = 4, message = "비밀번호는 4자리만 입력 가능합니다.")
	@Pattern(regexp = "^[0-9]{4}$", message = "계좌 비밀번호는 숫자만 입력 가능합니다.")
	@Schema(description = "계좌 비밀번호", example = "accountPassword")
	String pinNumber,

	@NotNull(message = "계좌 타입을 입력해주세요.")
	@Schema(description = "계좌 타입", example = "CHECKING, SAVINGS, DEPOSIT", allowableValues = {"DEPOSIT"})
	ProductType productType
) {
}
