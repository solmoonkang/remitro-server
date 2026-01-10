package com.remitro.account.presentation.dto.request;

import com.remitro.account.domain.account.enums.ProductType;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
@Schema(name = "DepositAccountOpenRequest", description = "입출금 계좌 개설 요청 DTO")
public record DepositAccountOpenRequest(
	@NotBlank(message = "계좌 이름을 입력해주세요.")
	@Size(max = 50, message = "계좌 이름은 50자 이내로 입력해주세요.")
	@Schema(description = "계좌 별칭", example = "accountName")
	String accountName,

	@NotBlank(message = "계좌 PIN을 입력해주세요.")
	@Size(min = 4, max = 4, message = "PIN은 4자리 숫자여야 합니다.")
	@Pattern(regexp = "\\d{4}", message = "PIN은 숫자만 입력 가능합니다.")
	@Schema(description = "계좌 PIN (숫자 4자리)", example = "1234")
	String pin,

	@NotNull(message = "상품 유형을 선택해주세요.")
	@Schema(description = "입출금 상품 유형", example = "CHECKING/SAVINGS")
	ProductType productType
) {
}
