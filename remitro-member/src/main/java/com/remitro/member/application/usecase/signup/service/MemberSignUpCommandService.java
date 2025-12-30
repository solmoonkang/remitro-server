package com.remitro.member.application.usecase.signup.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.member.application.usecase.signup.dto.request.SignUpRequest;
import com.remitro.member.application.common.validator.MemberValidator;
import com.remitro.member.domain.member.model.Member;
import com.remitro.member.domain.member.repository.MemberRepository;
import com.remitro.member.infrastructure.messaging.MemberEventPublisher;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberSignUpCommandService {

	private final MemberValidator memberValidator;
	private final PasswordEncoder passwordEncoder;
	private final MemberRepository memberRepository;
	private final MemberEventPublisher memberEventPublisher;

	@Transactional
	public void signUp(SignUpRequest signUpRequest) {
		memberValidator.validateUniqueEmail(signUpRequest.email());
		memberValidator.validateUniqueNickname(signUpRequest.nickname());
		memberValidator.validateUniquePhoneNumber(signUpRequest.phoneNumber());
		memberValidator.validatePasswordMatches(signUpRequest.password(), signUpRequest.checkPassword());

		final Member member = Member.create(
			signUpRequest.email(),
			passwordEncoder.encode(signUpRequest.password()),
			signUpRequest.nickname(),
			signUpRequest.phoneNumber()
		);

		memberRepository.save(member);
		memberEventPublisher.publishMemberCreated(member);
	}
}
