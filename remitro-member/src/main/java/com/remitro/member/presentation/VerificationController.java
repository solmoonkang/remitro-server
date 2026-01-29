package com.remitro.member.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.remitro.support.response.CommonResponse;
import com.remitro.member.application.command.dto.request.CodeSendRequest;
import com.remitro.member.application.command.dto.request.CodeVerifyRequest;
import com.remitro.member.application.command.dto.response.CodeConfirmResponse;
import com.remitro.member.application.command.dto.response.CodeIssueResponse;
import com.remitro.member.application.command.verification.VerificationCommandService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/verifications")
@RequiredArgsConstructor
@Tag(name = "VERIFICATION", description = "인증 번호 발송 및 검증 API")
public class VerificationController {

	private final VerificationCommandService verificationCommandService;

	@Operation(
		summary = "인증 번호 발급 및 발송",
		description = "입력한 이메일로 6자리 인증 번호를 발송합니다."
	)
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "인증 번호 발송 성공"),
		@ApiResponse(responseCode = "400", description = "잘못된 이메일 형식"),
		@ApiResponse(responseCode = "500", description = "서버 내부 오류")
	})
	@PostMapping("/send")
	@ResponseStatus(HttpStatus.CREATED)
	public CommonResponse<CodeIssueResponse> issueCode(
		@Valid @RequestBody CodeSendRequest codeSendRequest
	) {
		return CommonResponse.success(
			verificationCommandService.issueCode(codeSendRequest)
		);
	}

	@Operation(
		summary = "인증 번호 검증",
		description = "이메일과 인증 번호를 확인하고, 성공 시 일회용 토큰을 발급합니다."
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "인증 성공 및 토큰 발급"),
		@ApiResponse(responseCode = "400", description = "인증 번호 불일치 또는 만료"),
		@ApiResponse(responseCode = "404", description = "존재하지 않는 검증 정보"),
		@ApiResponse(responseCode = "409", description = "이미 완료된 인증 시도"),
		@ApiResponse(responseCode = "500", description = "서버 내부 오류")
	})
	@PostMapping("/verify")
	@ResponseStatus(HttpStatus.OK)
	public CommonResponse<CodeConfirmResponse> verifyCode(
		@Valid @RequestBody CodeVerifyRequest codeVerifyRequest
	) {
		return CommonResponse.success(
			verificationCommandService.verifyCode(codeVerifyRequest)
		);
	}
}
