package com.remitro.account.infrastructure.messaging;

import static com.remitro.account.infrastructure.constant.MemberEventType.*;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.account.domain.event.MemberCreatedEvent;
import com.remitro.account.domain.event.MemberKycUpdatedEvent;
import com.remitro.account.domain.event.MemberStatusUpdatedEvent;
import com.remitro.account.domain.model.MemberProjection;
import com.remitro.account.domain.repository.MemberProjectionRepository;
import com.remitro.common.contract.event.EventEnvelope;
import com.remitro.common.util.JsonMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class MemberEventConsumer {

	private final MemberProjectionRepository memberProjectionRepository;

	@KafkaListener(
		topics = "${topics.member-events}",
		groupId = "${spring.kafka.consumer.group-id}"
	)
	@Transactional
	public void handleConsumeEvent(EventEnvelope eventEnvelope) {
		log.info("[✅ LOGGER] MEMBER 이벤트를 수신했습니다: EVENT_ID={}, EVENT_TYPE={}",
			eventEnvelope.eventId(),
			eventEnvelope.eventType()
		);

		switch (eventEnvelope.eventType()) {
			case MEMBER_CREATED_EVENT -> handleMemberCreatedEvent(eventEnvelope.eventPayload());
			case MEMBER_STATUS_UPDATED_EVENT -> handleMemberStatusUpdatedEvent(eventEnvelope.eventPayload());
			case MEMBER_KYC_UPDATED_EVENT -> handleMemberKycUpdatedEvent(eventEnvelope.eventPayload());
			default -> log.warn("[✅ LOGGER] 알 수 없는 MEMBER 이벤트 타입을 수신했습니다: EVENT_TYPE={}",
				eventEnvelope.eventType()
			);
		}
	}

	private void handleMemberCreatedEvent(String eventPayload) {
		final MemberCreatedEvent memberCreatedEvent = JsonMapper.fromJSON(
			eventPayload,
			MemberCreatedEvent.class
		);

		final MemberProjection member = MemberProjection.create(
			memberCreatedEvent.memberId(),
			memberCreatedEvent.nickname(),
			memberCreatedEvent.activityStatus(),
			memberCreatedEvent.kycStatus(),
			memberCreatedEvent.isAccountOpenAllowed()
		);
		memberProjectionRepository.save(member);
	}

	private void handleMemberStatusUpdatedEvent(String eventPayload) {
		final MemberStatusUpdatedEvent memberStatusUpdatedEvent = JsonMapper.fromJSON(
			eventPayload,
			MemberStatusUpdatedEvent.class
		);

		memberProjectionRepository.findById(memberStatusUpdatedEvent.memberId())
			.ifPresent(member -> member.updateActivityAndKycStatus(
				memberStatusUpdatedEvent.activityStatus(),
				memberStatusUpdatedEvent.kycStatus(),
				memberStatusUpdatedEvent.isAccountOpenAllowed()
			));
	}

	private void handleMemberKycUpdatedEvent(String eventPayload) {
		final MemberKycUpdatedEvent memberKycUpdatedEvent = JsonMapper.fromJSON(
			eventPayload,
			MemberKycUpdatedEvent.class
		);

		memberProjectionRepository.findById(memberKycUpdatedEvent.memberId())
			.ifPresent(member -> member.updateActivityAndKycStatus(
				member.getActivityStatus(),
				memberKycUpdatedEvent.kycStatus(),
				memberKycUpdatedEvent.isAccountOpenAllowed()
			));
	}
}
