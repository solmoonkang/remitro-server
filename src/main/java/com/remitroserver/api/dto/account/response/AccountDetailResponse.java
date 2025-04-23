package com.remitroserver.api.dto.account.response;

import java.time.LocalDateTime;
import java.util.List;

import com.remitroserver.api.domain.account.model.AccountStatus;
import com.remitroserver.api.domain.account.model.AccountType;
import com.remitroserver.api.dto.transaction.response.TransactionSummaryResponse;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "계좌 상세 조회 응답 DTO")
public record AccountDetailResponse(
	@Schema(description = "계좌 번호", example = "110-01-123456")
	String accountNumber,

	@Schema(description = "계좌 유형", example = "CHECKING")
	AccountType accountType,

	@Schema(description = "계좌 잔액", example = "100,000")
	Long balance,

	@Schema(description = "계좌 상태", example = "ACTIVE")
	AccountStatus status,

	@Schema(description = "계좌 생성일", example = "2024-04-21T15:30:00")
	LocalDateTime createdAt,

	@Schema(description = "계좌 소유자 닉네임", example = "memberNickname")
	String ownerNickname,

	@Schema(
		description = "최근 거래 내역 요약 (이달 1일 ~ 오늘)",
		example = """
			[
				  {
					"transactionToken": "e8b9d16a-9f54-4a6f-8e8f-123456789abc",
					"fromAccountNumber": "110-01-123456",
					"toAccountNumber": "110-01-654321",
					"amount": 50000,
					"status": "COMPLETED",
					"createdAt": "2024-04-01T13:45:00"
				  },
				  {
					"transactionToken": "c2c34b15-9a22-42ef-97ff-abcde1234567",
					"fromAccountNumber": "110-01-123456",
					"toAccountNumber": "110-01-112233",
					"amount": 10000,
					"status": "CANCELLED",
					"createdAt": "2024-04-30T09:30:00"
				   },
				  ...
			]
			""")
	List<TransactionSummaryResponse> recentTransactions
) {
}
