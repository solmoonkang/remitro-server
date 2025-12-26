package com.remitro.member.application.service.member;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.remitro.common.security.Role;
import com.remitro.event.common.metadata.EventType;
import com.remitro.member.application.mapper.MemberEventMapper;
import com.remitro.member.domain.enums.AggregateType;
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

	public void publishMemberLocked(Member member, Long adminMemberId) {
		publish(
			member.getId(),
			EventType.MEMBER_LOCKED,
			MemberEventMapper.toMemberLockedEvent(member, adminMemberId)
		);
	}

	public void publishMemberUnlockedByAdmin(Member member, Long adminMemberId, LocalDateTime unlockedAt) {
		publish(
			member.getId(),
			EventType.MEMBER_UNLOCKED_BY_ADMIN,
			MemberEventMapper.toMemberUnlockedEventByAdmin(member, adminMemberId, unlockedAt)
		);
	}

	public void publishMemberUnlockedBySelf(Member member, LocalDateTime unlockedAt) {
		publish(
			member.getId(),
			EventType.MEMBER_UNLOCKED_BY_ADMIN,
			MemberEventMapper.toMemberUnlockedEventBySelf(member, unlockedAt)
		);
	}

	public void publishMemberDormant(Member member, LocalDateTime dormantAt) {
		publish(
			member.getId(),
			EventType.MEMBER_DORMANT,
			MemberEventMapper.toMemberDormantEvent(member, dormantAt)
		);
	}

	public void publishMemberActivated(Member member, LocalDateTime activatedAt) {
		publish(
			member.getId(),
			EventType.MEMBER_ACTIVATED,
			MemberEventMapper.toMemberActivatedEvent(member, activatedAt)
		);
	}

	public void publishMemberRoleChanged(
		Member member,
		Role previousRole,
		Long adminMemberId,
		LocalDateTime changedAt
	) {
		publish(
			member.getId(),
			EventType.MEMBER_ROLE_CHANGED,
			MemberEventMapper.toMemberRoleChangedEvent(member, previousRole, adminMemberId, changedAt)
		);
	}

	public void publishKycRequested(Member member, LocalDateTime requestedAt) {
		publish(
			member.getId(),
			EventType.MEMBER_KYC_REQUESTED,
			MemberEventMapper.toMemberKycRequestedEvent(member, requestedAt)
		);
	}

	public void publishKycVerified(Member member, Long adminMemberId, LocalDateTime verifiedAt) {
		publish(
			member.getId(),
			EventType.MEMBER_KYC_VERIFIED,
			MemberEventMapper.toMemberKycVerifiedEvent(member, adminMemberId, verifiedAt)
		);
	}

	public void publishKycRejected(Member member, Long adminMemberId, String reason, LocalDateTime rejectedAt) {
		publish(
			member.getId(),
			EventType.MEMBER_KYC_REJECTED,
			MemberEventMapper.toMemberKycRejectedEvent(member, adminMemberId, reason, rejectedAt)
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
