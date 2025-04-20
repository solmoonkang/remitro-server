package com.remitroserver.api.application.auth;

import static com.remitroserver.global.error.model.ErrorMessage.*;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitroserver.api.application.member.MemberReadService;
import com.remitroserver.api.domain.member.entity.Member;
import com.remitroserver.api.domain.member.repository.TokenRepository;
import com.remitroserver.api.dto.member.request.LoginRequest;
import com.remitroserver.api.dto.member.response.LoginResponse;
import com.remitroserver.global.auth.token.JwtProvider;
import com.remitroserver.global.error.exception.BadRequestException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthenticationService {

	private final PasswordEncoder passwordEncoder;
	private final JwtProvider jwtProvider;
	private final TokenRepository tokenRepository;
	private final MemberReadService memberReadService;

	@Transactional
	public LoginResponse loginMember(LoginRequest loginRequest) {
		final Member member = memberReadService.getMemberByEmail(loginRequest.email());

		validatePasswordMatches(loginRequest.password(), member.getPassword());

		final String accessToken = jwtProvider.generateAccessToken(member.getEmail(), member.getNickname());
		final String refreshToken = jwtProvider.generateRefreshToken(member.getEmail());

		tokenRepository.saveToken(member.getEmail(), refreshToken);

		return new LoginResponse(accessToken, refreshToken);
	}

	private void validatePasswordMatches(String rawPassword, String encodedPassword) {
		if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
			throw new BadRequestException(PASSWORD_MISMATCH_ERROR);
		}
	}
}
