package com.remitro.member.application.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.remitro.common.domain.enums.AggregateType;
import com.remitro.common.domain.enums.EventType;
import com.remitro.common.infra.util.JsonUtil;
import com.remitro.member.application.dto.request.SignUpRequest;
import com.remitro.member.application.mapper.MemberMapper;
import com.remitro.member.domain.model.Member;
import com.remitro.member.domain.model.OutboxMessage;
import com.remitro.member.domain.repository.MemberRepository;
import com.remitro.member.domain.repository.OutboxMessageRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberWriteService {

	private final PasswordEncoder passwordEncoder;
	private final MemberRepository memberRepository;
	private final OutboxMessageRepository outboxMessageRepository;

	public void saveMember(SignUpRequest signUpRequest) {
		final Member member = saveMemberMetadata(signUpRequest);
		saveMemberActivityEvent(member);
	}

	private Member saveMemberMetadata(SignUpRequest signUpRequest) {
		final String encodedPassword = passwordEncoder.encode(signUpRequest.password());
		final Member member = Member.create(
			signUpRequest.email(), encodedPassword, signUpRequest.nickname(), signUpRequest.phoneNumber());
		return memberRepository.save(member);
	}

	private void saveMemberActivityEvent(Member member) {
		final String eventMessage = JsonUtil.toJSON(MemberMapper.toMemberStatusChangedEvent(member));
		final OutboxMessage outboxMessage = OutboxMessage.create(
			member.getId(), AggregateType.MEMBER, EventType.MEMBER_ACTIVITY_UPSERTED_EVENT, eventMessage);
		outboxMessageRepository.save(outboxMessage);
	}
}
