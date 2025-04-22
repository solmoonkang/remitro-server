package com.remitroserver.api.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.remitroserver.api.application.auth.AuthenticationService;
import com.remitroserver.api.dto.member.request.LoginRequest;
import com.remitroserver.api.dto.member.response.LoginResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "인증 APIs", description = "로그인, 토큰 재발급 기능을 제공합니다.")
public class AuthenticationController {

	private final AuthenticationService authenticationService;

	@PostMapping("/login")
	@ResponseStatus(HttpStatus.OK)
	@Operation(
		summary = "로그인 - 사용자 인증 및 토큰 발급",
		description = "사용자의 이메일과 비밀번호를 검증한 뒤, AccessToken과 RefreshToken을 발급합니다."
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "✅ 로그인 성공 및 토큰 발급"),
		@ApiResponse(responseCode = "400", description = "❌ 형식 오류 또는 비밀번호 불일치"),
		@ApiResponse(responseCode = "404", description = "🔍 존재하지 않는 사용자"),
		@ApiResponse(responseCode = "500", description = "💥 서버 내부 오류")
	})
	public ResponseEntity<LoginResponse> loginMember(@RequestBody @Valid LoginRequest loginRequest) {
		return ResponseEntity.ok().body(authenticationService.loginMember(loginRequest));
	}

	@PostMapping("/reissue")
	@ResponseStatus(HttpStatus.OK)
	@Operation(
		summary = "토큰 재발급 - RefreshToken을 통한 AccessToken 재발급",
		description = "요청 헤더에 있는 RefreshToken을 검증한 뒤, 새로운 AccessToken과 RefreshToken을 발급합니다."
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "✅ 토큰 재발급 성공"),
		@ApiResponse(responseCode = "401", description = "🔒 토큰이 유효하지 않거나 재사용된 경우"),
		@ApiResponse(responseCode = "404", description = "🔍 존재하지 않는 사용자"),
		@ApiResponse(responseCode = "500", description = "💥 서버 내부 오류")
	})
	public ResponseEntity<LoginResponse> reissueToken(HttpServletRequest httpServletRequest) {
		return ResponseEntity.ok().body(authenticationService.reissueToken(httpServletRequest));
	}
}
