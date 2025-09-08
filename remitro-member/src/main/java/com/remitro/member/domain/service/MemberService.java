package com.remitro.member.domain.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.common.error.exception.BadRequestException;
import com.remitro.common.error.exception.ConflictException;
import com.remitro.common.error.model.ErrorMessage;
import com.remitro.member.application.dto.request.SignUpRequest;
import com.remitro.member.domain.model.Member;
import com.remitro.member.domain.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

	private final PasswordEncoder passwordEncoder;
	private final MemberRepository memberRepository;

	@Transactional
	public void signUpMember(SignUpRequest signUpRequest) {
		validateEmailNotDuplicated(signUpRequest.email());
		validateNicknameNotDuplicated(signUpRequest.nickname());
		validatePasswordMatche(signUpRequest.password(), signUpRequest.checkPassword());

		final String encodedPassword = passwordEncoder.encode(signUpRequest.password());
		final Member member = Member.createMember(signUpRequest.email(), encodedPassword, signUpRequest.nickname());

		memberRepository.save(member);
	}

	private void validateEmailNotDuplicated(String email) {
		if (memberRepository.existsByEmail(email)) {
			throw new ConflictException(ErrorMessage.EMAIL_DUPLICATED);
		}
	}

	private void validateNicknameNotDuplicated(String nickname) {
		if (memberRepository.existsByNickname(nickname)) {
			throw new ConflictException(ErrorMessage.NICKNAME_DUPLICATED);
		}
	}

	private void validatePasswordMatche(String password, String checkPassword) {
		if (!password.equals(checkPassword)) {
			throw new BadRequestException(ErrorMessage.INVALID_PASSWORD);
		}
	}
}
