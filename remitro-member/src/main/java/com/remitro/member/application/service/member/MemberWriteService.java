package com.remitro.member.application.service.member;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.remitro.common.util.JsonMapper;
import com.remitro.member.application.dto.request.SignUpRequest;
import com.remitro.member.application.mapper.MemberEventMapper;
import com.remitro.member.domain.enums.ActivityStatus;
import com.remitro.member.domain.enums.AggregateType;
import com.remitro.member.domain.enums.KycStatus;
import com.remitro.member.domain.event.EventType;
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
		requestMemberCreatedEvent(member);
	}

	public void updateActivityStatus(Member member, ActivityStatus activityStatus) {
		member.updateActivityStatus(activityStatus);
		requestMemberStatusUpdatedEvent(member);
	}

	public void updateKycStatus(Member member, KycStatus kycStatus) {
		member.updateKycStatus(kycStatus);
		requestMemberKycStatusUpdatedEvent(member);
	}

	private void requestMemberCreatedEvent(Member member) {
		final String eventPayload = JsonMapper.toJSON(MemberEventMapper.toMemberCreatedEvent(member));
		saveOutboxMessageMetadata(member, EventType.MEMBER_CREATED, eventPayload);
	}

	private void requestMemberStatusUpdatedEvent(Member member) {
		final String eventPayload = JsonMapper.toJSON(MemberEventMapper.toMemberStatusUpdatedEvent(member));
		saveOutboxMessageMetadata(member, EventType.MEMBER_STATUS_UPDATED, eventPayload);
	}

	private void requestMemberKycStatusUpdatedEvent(Member member) {
		final String eventPayload = JsonMapper.toJSON(MemberEventMapper.toMemberKycUpdatedEvent(member));
		saveOutboxMessageMetadata(member, EventType.MEMBER_KYC_UPDATED, eventPayload);
	}

	private Member saveMemberMetadata(SignUpRequest signUpRequest) {
		final Member member = Member.create(
			signUpRequest.email(),
			passwordEncoder.encode(signUpRequest.password()),
			signUpRequest.nickname(),
			signUpRequest.phoneNumber()
		);
		return memberRepository.save(member);
	}

	private void saveOutboxMessageMetadata(Member member, EventType eventType, String eventPayload) {
		final OutboxMessage outboxMessage = OutboxMessage.create(
			member.getId(),
			AggregateType.MEMBER,
			eventType,
			eventPayload
		);
		outboxMessageRepository.save(outboxMessage);
	}
}
