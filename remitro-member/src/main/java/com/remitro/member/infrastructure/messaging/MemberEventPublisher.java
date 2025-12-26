package com.remitro.member.infrastructure.messaging;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.remitro.common.security.Role;
import com.remitro.event.common.metadata.EventType;
import com.remitro.event.member.enums.LockActorType;
import com.remitro.event.member.enums.UnlockActorType;
import com.remitro.member.application.mapper.MemberEventMapper;
import com.remitro.member.domain.enums.AggregateType;
import com.remitro.member.domain.enums.LockReason;
import com.remitro.member.domain.model.Member;
import com.remitro.member.domain.model.OutboxMessage;
import com.remitro.member.domain.repository.OutboxMessageRepository;
import com.remitro.member.infrastructure.support.JsonMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MemberEventPublisher {

	private final OutboxMessageRepository outboxMessageRepository;

	public void publishMemberCreated(Member member) {
		publish(
			member.getId(),
			EventType.MEMBER_CREATED,
			MemberEventMapper.toMemberCreatedEvent(member)
		);
	}

	public void publishMemberLocked(
		Member member,
		Long adminMemberId,
		LockActorType lockActorType,
		LockReason lockReason,
		LocalDateTime occurredAt
	) {
		publish(
			member.getId(),
			EventType.MEMBER_LOCKED,
			MemberEventMapper.toMemberLockedEvent(member, adminMemberId, lockActorType, lockReason, occurredAt)
		);
	}

	public void publishMemberUnlocked(
		Member member,
		Long adminMemberId,
		UnlockActorType unlockActorType,
		LocalDateTime occurredAt
	) {
		publish(
			member.getId(),
			EventType.MEMBER_UNLOCKED,
			MemberEventMapper.toMemberUnlockedEvent(member, adminMemberId, unlockActorType, occurredAt)
		);
	}

	public void publishMemberDormant(Member member, LocalDateTime occurredAt) {
		publish(
			member.getId(),
			EventType.MEMBER_DORMANT,
			MemberEventMapper.toMemberDormantEvent(member, occurredAt)
		);
	}

	public void publishMemberActivated(Member member, LocalDateTime occurredAt) {
		publish(
			member.getId(),
			EventType.MEMBER_ACTIVATED,
			MemberEventMapper.toMemberActivatedEvent(member, occurredAt)
		);
	}

	public void publishMemberRoleChanged(
		Member member,
		Role previousRole,
		Long adminMemberId,
		LocalDateTime occurredAt
	) {
		publish(
			member.getId(),
			EventType.MEMBER_ROLE_CHANGED,
			MemberEventMapper.toMemberRoleChangedEvent(member, previousRole, adminMemberId, occurredAt)
		);
	}

	public void publishKycRequested(Member member, LocalDateTime occurredAt) {
		publish(
			member.getId(),
			EventType.MEMBER_KYC_REQUESTED,
			MemberEventMapper.toMemberKycRequestedEvent(member, occurredAt)
		);
	}

	public void publishKycVerified(Member member, Long adminMemberId, LocalDateTime occurredAt) {
		publish(
			member.getId(),
			EventType.MEMBER_KYC_VERIFIED,
			MemberEventMapper.toMemberKycVerifiedEvent(member, adminMemberId, occurredAt)
		);
	}

	public void publishKycRejected(Member member, Long adminMemberId, String reason, LocalDateTime occurredAt) {
		publish(
			member.getId(),
			EventType.MEMBER_KYC_REJECTED,
			MemberEventMapper.toMemberKycRejectedEvent(member, adminMemberId, reason, occurredAt)
		);
	}

	private void publish(Long aggregateId, EventType eventType, Object payload) {
		final OutboxMessage outboxMessage = OutboxMessage.create(
			aggregateId,
			AggregateType.MEMBER,
			eventType,
			JsonMapper.toJSON(payload)
		);

		outboxMessageRepository.save(outboxMessage);
	}
}
