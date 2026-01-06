package com.remitro.member.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
@Schema(name = "KycRejectRequest", description = "KYC 거절 사유 요청 DTO")
public record KycRejectRequest(
	@NotBlank(message = "거절 사유를 입력해주세요.")
	@Schema(description = "KYC 거절 사유", example = "kycRejectReason")
	String reason
) {
}
