package com.remitro.member.application.dto.request;

import com.remitro.member.domain.enums.KycStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "회원 KYC 상태 변경 요청 DTO")
public record UpdateKycStatusRequest(
	@Schema(description = "변경할 KYC 상태", example = "VERIFIED")
	KycStatus kycStatus,

	@Schema(description = "KYC 실패/보류 사유", example = "신분증 사진 불일치")
	String reason
) {
}
