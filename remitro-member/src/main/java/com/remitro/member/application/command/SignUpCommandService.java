package com.remitro.member.application.command;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.member.application.command.dto.request.SignUpRequest;
import com.remitro.member.domain.member.model.Member;
import com.remitro.member.domain.member.policy.MemberSignUpPolicy;
import com.remitro.member.domain.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class SignUpCommandService {

	private final MemberRepository memberRepository;
	private final MemberSignUpPolicy memberSignUpPolicy;

	private final PasswordEncoder passwordEncoder;

	public void signUp(SignUpRequest signUpRequest) {
		memberSignUpPolicy.validateUniqueness(
			signUpRequest.email(),
			signUpRequest.nickname(),
			signUpRequest.phoneNumber()
		);

		final Member member = Member.register(
			signUpRequest.email(),
			passwordEncoder.encode(signUpRequest.password()),
			signUpRequest.nickname(),
			signUpRequest.phoneNumber()
		);

		memberRepository.save(member);
	}
}
