package com.remitro.auth.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.auth.domain.repository.TokenRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LogoutService {

	private final TokenRepository tokenRepository;

	@Transactional
	public void logout(Long memberId, String deviceId) {
		tokenRepository.revokeByMemberAndDevice(memberId, deviceId);
	}

	@Transactional
	public void logoutAll(Long memberId) {
		tokenRepository.revokeAllByMember(memberId);
	}
}
