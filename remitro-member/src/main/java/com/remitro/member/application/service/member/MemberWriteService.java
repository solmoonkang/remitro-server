package com.remitro.member.application.service.member;

import java.time.Clock;
import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.remitro.common.security.Role;
import com.remitro.member.application.dto.request.SignUpRequest;
import com.remitro.member.domain.model.Member;
import com.remitro.member.domain.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberWriteService {

	private final PasswordEncoder passwordEncoder;
	private final MemberRepository memberRepository;
	private final MemberEventPublisher memberEventPublisher;
	private final Clock clock;

	public void register(SignUpRequest signUpRequest) {
		final Member member = Member.create(
			signUpRequest.email(),
			passwordEncoder.encode(signUpRequest.password()),
			signUpRequest.nickname(),
			signUpRequest.phoneNumber()
		);

		memberRepository.save(member);
		memberEventPublisher.publishMemberCreated(member);
	}

	public void changeRole(Member member, Role nextRole, Long adminMemberId) {
		final Role previousRole = member.getRole();
		member.changeRole(nextRole);
		memberEventPublisher.publishMemberRoleChanged(member, previousRole, adminMemberId, LocalDateTime.now(clock));
	}
}
