package com.remitro.member.domain.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.member.application.dto.request.SignUpRequest;
import com.remitro.member.domain.model.Member;
import com.remitro.member.domain.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;

	@Transactional
	public void signUpMember(SignUpRequest signUpRequest) {
		validateDuplicateEmail(signUpRequest.email());
		validateDuplicateNickname(signUpRequest.nickname());
		validatePasswordMatches(signUpRequest.password(), signUpRequest.checkPassword());

		final String encodedPassword = passwordEncoder.encode(signUpRequest.password());
		final Member member = Member.createMember(signUpRequest.email(), encodedPassword, signUpRequest.nickname());

		memberRepository.save(member);
	}

	private void validateDuplicateEmail(String email) {
		if (memberRepository.existsByEmail(email)) {
			throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
		}
	}

	private void validateDuplicateNickname(String nickname) {
		if (memberRepository.existsByNickname(nickname)) {
			throw new IllegalArgumentException("이미 존재하는 닉네임입니다.");
		}
	}

	private void validatePasswordMatches(String password, String checkPassword) {
		if (!password.equals(checkPassword)) {
			throw new IllegalArgumentException("비밀번호와 확인 비밀번호가 서로 일치하지 않습니다.");
		}
	}
}
