package com.remitro.member.application.command.signup;

import java.time.Clock;
import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.event.common.EventType;
import com.remitro.member.application.command.dto.request.SignUpRequest;
import com.remitro.member.application.mapper.EventMapper;
import com.remitro.member.application.outbox.OutboxEventRecorder;
import com.remitro.member.domain.member.model.Member;
import com.remitro.member.domain.member.repository.MemberRepository;
import com.remitro.support.util.DataHasher;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class SignUpCommandService {

	private final MemberRepository memberRepository;
	private final SignUpValidator signUpValidator;

	private final OutboxEventRecorder outboxEventRecorder;

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

		outboxEventRecorder.record(
			EventType.MEMBER_REGISTERED,
			member.getId(),
			EventMapper.toMemberRegisteredEvent(member, now)
		);
	}
}
