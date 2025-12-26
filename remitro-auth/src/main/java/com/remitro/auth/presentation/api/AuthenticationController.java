package com.remitro.auth.presentation.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.remitro.auth.application.dto.request.LoginRequest;
import com.remitro.auth.application.dto.response.TokenResponse;
import com.remitro.auth.application.service.AuthenticationService;
import com.remitro.auth.application.service.LogoutService;
import com.remitro.auth.application.service.TokenReissueService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "사용자 인증 APIs", description = "로그인 및 토큰 관련 API")
public class AuthenticationController {

	private final AuthenticationService authenticationService;
	private final TokenReissueService tokenReissueService;
	private final LogoutService logoutService;

	@PostMapping("/login")
	@ResponseStatus(HttpStatus.OK)
	@Operation(
		summary = "사용자 로그인",
		description = "사용자 로그인을 진행합니다."
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "🎉 로그인 성공",
			content = @Content(schema = @Schema(implementation = TokenResponse.class))
		),
		@ApiResponse(responseCode = "401", description = "🔒 인증 실패"),
		@ApiResponse(responseCode = "404", description = "🔍 존재하지 않는 사용자"),
		@ApiResponse(responseCode = "500", description = "💥 서버 내부 오류")
	})
	public TokenResponse login(
		@RequestHeader("X-Device-Id") String deviceId,
		@Valid @RequestBody LoginRequest loginRequest
	) {
		return authenticationService.login(deviceId, loginRequest);
	}

	@PostMapping("/reissue")
	@ResponseStatus(HttpStatus.OK)
	@Operation(
		summary = "토큰 재발급",
		description = "리프레시 토큰으로 새로운 토큰을 재발급합니다."
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "🎉 토큰 재발급 성공",
			content = @Content(schema = @Schema(implementation = TokenResponse.class))
		),
		@ApiResponse(responseCode = "400", description = "❌ 유효하지 않은 토큰"),
		@ApiResponse(responseCode = "401", description = "🔒 인증되지 않은 토큰"),
		@ApiResponse(responseCode = "500", description = "💥 서버 내부 오류")
	})
	public TokenResponse reissue(@RequestHeader("Authorization") String authorization) {
		return tokenReissueService.reissueTokens(authorization);
	}

	@PostMapping("/logout")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Operation(
		summary = "로그아웃",
		description = "현재 기기에서 로그아웃합니다."
	)
	@ApiResponses({
		@ApiResponse(responseCode = "204", description = "🎉 로그아웃 성공"),
		@ApiResponse(responseCode = "401", description = "🔒 인증되지 않은 사용자"),
		@ApiResponse(responseCode = "500", description = "💥 서버 내부 오류")
	})
	public void logout(@RequestHeader("X-Member-Id") Long memberId, @RequestHeader("X-Device-Id") String deviceId) {
		logoutService.logout(memberId, deviceId);
	}

	@PostMapping("/logout/all")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Operation(
		summary = "전체 로그아웃",
		description = "모든 기기에서 로그아웃합니다."
	)
	@ApiResponses({
		@ApiResponse(responseCode = "204", description = "🎉 전체 로그아웃 성공"),
		@ApiResponse(responseCode = "401", description = "🔒 인증되지 않은 사용자"),
		@ApiResponse(responseCode = "500", description = "💥 서버 내부 오류")
	})
	public void logoutAll(@RequestHeader("X-Member-Id") Long memberId) {
		logoutService.logoutAll(memberId);
	}
}
