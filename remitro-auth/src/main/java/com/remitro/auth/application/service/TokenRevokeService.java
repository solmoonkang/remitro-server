package com.remitro.auth.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.auth.infrastructure.persistence.RefreshTokenRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenRevokeService {

	private final RefreshTokenRepository refreshTokenRepository;

	@Transactional
	public void logout(Long memberId, String deviceId) {
		refreshTokenRepository.revokeByMemberAndDevice(memberId, deviceId);
	}

	@Transactional
	public void logoutAll(Long memberId) {
		refreshTokenRepository.revokeAllByMember(memberId);
	}
}
