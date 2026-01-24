package com.remitro.member.application.command;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.member.application.command.dto.request.PasswordChangeRequest;
import com.remitro.member.application.query.MemberFinder;
import com.remitro.member.application.validator.PasswordValidator;
import com.remitro.member.domain.member.model.Member;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PasswordCommandService {

	private final MemberFinder memberFinder;
	private final PasswordValidator passwordValidator;
	private final PasswordEncoder passwordEncoder;

	@CacheEvict(value = "memberProfile", key = "'ID:' + #memberId")
	public void changePassword(Long memberId, PasswordChangeRequest passwordChangeRequest) {
		final Member member = memberFinder.getMemberById(memberId);

		passwordValidator.validatePasswordChange(
			member.getPasswordHash(),
			passwordChangeRequest.currentPassword(),
			passwordChangeRequest.newPassword(),
			passwordChangeRequest.confirmPassword()
		);

		final String encodedPassword = passwordEncoder.encode(passwordChangeRequest.newPassword());
		member.changePassword(encodedPassword);
	}
}
