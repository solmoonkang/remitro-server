package com.remitro.member.application.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.remitro.member.application.dto.request.LoginRequest;
import com.remitro.member.application.dto.response.TokenResponse;
import com.remitro.member.domain.service.auth.AuthenticationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "사용자 인증 APIs", description = "로그인 및 재발급 관련 API")
public class AuthenticationController {

	private final AuthenticationService authenticationService;

	@PostMapping("/login")
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "사용자 로그인", description = "사용자 로그인을 진행합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "🎉 로그인 성공"),
		@ApiResponse(responseCode = "404", description = "🔍 존재하지 않는 사용자"),
		@ApiResponse(responseCode = "500", description = "💥 서버 내부 오류")
	})
	public ResponseEntity<TokenResponse> loginMember(@Valid @RequestBody LoginRequest loginRequest) {
		return ResponseEntity.ok().body(authenticationService.loginMember(loginRequest));
	}

	@PostMapping("/reissue")
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "토큰 재발급", description = "리프레시 토큰을 통해 새로운 액세스/리프레시 토큰을 재발급합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "🎉 토큰 재발급 성공"),
		@ApiResponse(responseCode = "400", description = "❌ 유효하지 않은 토큰"),
		@ApiResponse(responseCode = "401", description = "🔒 인증되지 않은 토큰"),
		@ApiResponse(responseCode = "404", description = "🔍 존재하지 않는 사용자"),
		@ApiResponse(responseCode = "500", description = "💥 서버 내부 오류")
	})
	public ResponseEntity<TokenResponse> reissueTokens(@RequestHeader("Authorization") String refreshToken) {
		return ResponseEntity.ok().body(authenticationService.reissueTokens(refreshToken));
	}
}
