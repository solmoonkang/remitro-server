package com.remitro.member.application.command;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.member.application.support.MemberFinder;
import com.remitro.member.domain.member.model.Member;
import com.remitro.member.domain.member.policy.PasswordPolicy;
import com.remitro.member.presentation.dto.request.ChangePasswordRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ChangePasswordCommandService {

	private final MemberFinder memberFinder;

	private final PasswordPolicy passwordPolicy;
	private final PasswordEncoder passwordEncoder;

	public void changePassword(Long memberId, ChangePasswordRequest changePasswordRequest) {
		final Member member = memberFinder.getById(memberId);

		passwordPolicy.validateChangeable(member, changePasswordRequest);

		member.changePassword(passwordEncoder.encode(changePasswordRequest.newPassword()));
	}
}
