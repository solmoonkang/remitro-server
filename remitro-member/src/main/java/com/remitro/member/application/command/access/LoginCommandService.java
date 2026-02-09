package com.remitro.member.application.command.access;

import java.time.Clock;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.support.exception.BaseException;
import com.remitro.member.application.dto.request.LoginRequest;
import com.remitro.member.application.dto.response.TokenResponse;
import com.remitro.member.application.read.MemberFinder;
import com.remitro.member.domain.member.model.Member;
import com.remitro.member.infrastructure.web.NetworkExtractor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoginCommandService {

	private final MemberFinder memberFinder;
	private final LoginProcessor loginProcessor;
	private final NetworkExtractor networkExtractor;
	private final Clock clock;

	@Transactional(noRollbackFor = BaseException.class)
	public TokenResponse login(
		LoginRequest loginRequest,
		HttpServletRequest httpServletRequest,
		HttpServletResponse httpServletResponse
	) {
		final Member member = memberFinder.getActiveByEmail(loginRequest.email());
		final LoginClientInfo loginClientInfo = networkExtractor.extractClientInfo(httpServletRequest);

		return loginProcessor.login(
			member, loginRequest.password(), loginClientInfo, LocalDateTime.now(clock), httpServletResponse
		);
	}
}
