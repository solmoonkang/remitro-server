package com.remitro.member.application.dto.request;

import com.remitro.member.domain.enums.ActivityStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "회원 활동 상태 변경 요청 DTO")
public record UpdateActivityStatusRequest(
	@Schema(description = "변경할 활동 상태", example = "DORMANT")
	ActivityStatus activityStatus
) {
}
