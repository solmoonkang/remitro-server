package com.remitro.member.application.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.remitro.member.application.dto.request.LoginRequest;
import com.remitro.member.application.dto.response.TokenResponse;
import com.remitro.member.domain.service.auth.AuthenticationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthenticationController {

	private final AuthenticationService authenticationService;

	@PostMapping("/login")
	public ResponseEntity<TokenResponse> loginMember(@Valid @RequestBody LoginRequest loginRequest) {
		return ResponseEntity.ok().body(authenticationService.loginMember(loginRequest));
	}

	@PostMapping("/reissue")
	public ResponseEntity<TokenResponse> reissueTokens(@RequestHeader("Authorization") String refreshToken) {
		return ResponseEntity.ok().body(authenticationService.reissueTokens(refreshToken));
	}
}
