package com.remitro.member.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.remitro.common.response.CommonResponse;
import com.remitro.member.application.command.LoginCommandService;
import com.remitro.member.application.command.dto.request.LoginRequest;
import com.remitro.member.application.command.dto.response.LoginResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "AUTH", description = "로그인 및 토큰 재발급 등 인증 관련 API")
public class AuthController {

	private final LoginCommandService loginCommandService;

	@Operation(
		summary = "로그인",
		description = "인증된 사용자가 로그인합니다."
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "로그인 성공"),
		@ApiResponse(responseCode = "401", description = "토큰 및 비밀번호 불일치"),
		@ApiResponse(responseCode = "404", description = "존재하지 않은 사용자"),
		@ApiResponse(responseCode = "500", description = "서버 내부 오류"),
	})
	@PostMapping("/login")
	@ResponseStatus(HttpStatus.OK)
	public CommonResponse<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
		return CommonResponse.success(loginCommandService.login(loginRequest));
	}
}
