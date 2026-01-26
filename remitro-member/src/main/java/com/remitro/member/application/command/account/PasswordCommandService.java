package com.remitro.member.application.command.account;

import java.time.Clock;
import java.time.LocalDateTime;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.member.application.command.account.validator.PasswordValidator;
import com.remitro.member.application.command.dto.request.PasswordChangeRequest;
import com.remitro.member.application.command.dto.request.PasswordRecoveryRequest;
import com.remitro.member.application.read.account.MemberFinder;
import com.remitro.member.application.read.verification.VerificationFinder;
import com.remitro.member.domain.member.model.Member;
import com.remitro.member.domain.verification.model.Verification;
import com.remitro.member.domain.verification.repository.VerificationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PasswordCommandService {

	private final MemberFinder memberFinder;
	private final VerificationFinder verificationFinder;
	private final VerificationRepository verificationRepository;
	private final PasswordValidator passwordValidator;

	private final CacheManager cacheManager;
	private final PasswordEncoder passwordEncoder;
	private final Clock clock;

	public void changePassword(Long memberId, PasswordChangeRequest passwordChangeRequest) {
		final Member member = memberFinder.getMemberById(memberId);

		passwordValidator.validatePasswordChange(
			member.getPasswordHash(),
			passwordChangeRequest.currentPassword(),
			passwordChangeRequest.newPassword(),
			passwordChangeRequest.confirmPassword()
		);

		final String encodedPassword = passwordEncoder.encode(passwordChangeRequest.newPassword());
		member.changePassword(encodedPassword, LocalDateTime.now(clock));

		evictMemberProfileCache(memberId);
	}

	public void recoveryPassword(PasswordRecoveryRequest passwordRecoveryRequest) {
		passwordValidator.validatePasswordConfirm(
			passwordRecoveryRequest.newPassword(),
			passwordRecoveryRequest.confirmPassword()
		);

		final Verification verification = verificationFinder.getVerifiedToken(
			passwordRecoveryRequest.email(),
			passwordRecoveryRequest.verificationToken()
		);

		final Member member = memberFinder.getMemberByEmail(passwordRecoveryRequest.email());
		final String encodedPassword = passwordEncoder.encode(passwordRecoveryRequest.newPassword());
		member.recoverPassword(encodedPassword, LocalDateTime.now(clock));

		evictMemberProfileCache(member.getId());

		verificationRepository.delete(verification);
	}

	private void evictMemberProfileCache(Long memberId) {
		Cache profileCache = cacheManager.getCache("memberProfile");
		if (profileCache != null) {
			profileCache.evict("ID:" + memberId);
		}
	}
}
