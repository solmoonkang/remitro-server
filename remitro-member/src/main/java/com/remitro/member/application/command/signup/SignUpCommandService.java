package com.remitro.member.application.command.signup;

import java.time.Clock;
import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.support.util.DataHasher;
import com.remitro.member.application.command.dto.request.SignUpRequest;
import com.remitro.member.domain.member.model.Member;
import com.remitro.member.domain.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class SignUpCommandService {

	private final MemberRepository memberRepository;
	private final SignUpValidator signUpValidator;
	private final PasswordEncoder passwordEncoder;
	private final DataHasher dataHasher;
	private final Clock clock;

	public void signUp(SignUpRequest signUpRequest) {
		final LocalDateTime now = LocalDateTime.now(clock);

		signUpValidator.validateSignUpUniqueness(
			signUpRequest.email(),
			signUpRequest.nickname(),
			signUpRequest.phoneNumber()
		);

		final String phoneNumberHash = dataHasher.hash(signUpRequest.phoneNumber());
		memberRepository.findWithdrawnByPhoneNumberHash(phoneNumberHash)
			.ifPresent(member -> signUpValidator.validateRejoinRestriction(member, now));

		final Member member = Member.register(
			signUpRequest.email(),
			passwordEncoder.encode(signUpRequest.password()),
			signUpRequest.nickname(),
			signUpRequest.phoneNumber(),
			phoneNumberHash,
			now
		);

		memberRepository.save(member);
	}
}
