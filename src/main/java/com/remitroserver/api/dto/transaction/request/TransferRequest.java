package com.remitroserver.api.dto.transaction.request;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
@Schema(description = "송금 요청 DTO")
public record TransferRequest(
	@Schema(description = "출금 계좌의 UUID", example = "a4b159da-fd5e-42cd-b6a9-1d09f5df92d5")
	@NotNull(message = "출금 계좌 토큰은 필수 항목입니다.")
	UUID fromAccountToken,

	@Schema(description = "입금 계좌 번호", example = "110-01-123456")
	@NotBlank(message = "수신 계좌 번호는 필수 항목입니다.")
	String toAccountNumber,

	@Schema(description = "송금 금액", example = "10,000")
	@NotNull(message = "송금 금액은 필수 항목입니다.")
	@Positive(message = "송금 금액은 0원보다 커야 합니다.")
	Long amount,

	@Schema(description = "멱등성 키", example = "req-uuid-unique-key")
	@NotBlank(message = "멱등성 키는 필수 항목입니다.")
	String idempotencyKey
) {
}
