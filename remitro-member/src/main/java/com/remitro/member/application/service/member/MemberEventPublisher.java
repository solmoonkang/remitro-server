package com.remitro.member.application.service.member;

import org.springframework.stereotype.Component;

import com.remitro.common.security.Role;
import com.remitro.member.application.mapper.MemberEventMapper;
import com.remitro.member.domain.enums.AggregateType;
import com.remitro.member.domain.event.EventType;
import com.remitro.member.domain.model.Member;
import com.remitro.member.domain.model.OutboxMessage;
import com.remitro.member.domain.repository.OutboxMessageRepository;
import com.remitro.member.infrastructure.support.JsonMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MemberEventPublisher {

	private final OutboxMessageRepository outboxMessageRepository;

	public void publishCreated(Member member) {
		saveOutboxMessage(
			member.getId(),
			EventType.MEMBER_CREATED,
			JsonMapper.toJSON(MemberEventMapper.toMemberCreatedEvent(member))
		);
	}

	public void publishActivityStatusUpdated(Member member) {
		saveOutboxMessage(
			member.getId(),
			EventType.MEMBER_ACTIVITY_STATUS_UPDATED,
			JsonMapper.toJSON(MemberEventMapper.toMemberStatusUpdatedEvent(member))
		);
	}

	public void publishKycRequested(Member member) {
		saveOutboxMessage(
			member.getId(),
			EventType.MEMBER_KYC_REQUESTED,
			JsonMapper.toJSON(MemberEventMapper.toMemberKycRequestedEvent(member))
		);
	}

	public void publishKycVerified(Member member, Long adminMemberId) {
		saveOutboxMessage(
			member.getId(),
			EventType.MEMBER_KYC_VERIFIED,
			JsonMapper.toJSON(MemberEventMapper.toMemberKycVerifiedEvent(member, adminMemberId))
		);
	}

	public void publishKycRejected(Member member, Long adminMemberId, String reason) {
		saveOutboxMessage(
			member.getId(),
			EventType.MEMBER_KYC_REJECTED,
			JsonMapper.toJSON(MemberEventMapper.toMemberKycRejectedEvent(member, adminMemberId, reason))
		);
	}

	public void publishRoleGranted(Member member, Role previousRole, Long adminMemberId) {
		saveOutboxMessage(
			member.getId(),
			EventType.MEMBER_ROLE_GRANTED,
			JsonMapper.toJSON(MemberEventMapper.toMemberRoleUpdatedEvent(member, previousRole, adminMemberId))
		);
	}

	public void publishRoleRevoked(Member member, Role previousRole, Long adminMemberId) {
		saveOutboxMessage(
			member.getId(),
			EventType.MEMBER_ROLE_REVOKED,
			JsonMapper.toJSON(MemberEventMapper.toMemberRoleUpdatedEvent(member, previousRole, adminMemberId))
		);
	}

	private void saveOutboxMessage(Long aggregateId, EventType eventType, Object domainEvent) {
		final String eventPayload = JsonMapper.toJSON(domainEvent);

		final OutboxMessage outboxMessage = OutboxMessage.create(
			aggregateId,
			AggregateType.MEMBER,
			eventType,
			eventPayload
		);

		outboxMessageRepository.save(outboxMessage);
	}
}
