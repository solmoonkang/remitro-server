package com.remitro.member.application.event;

import org.springframework.stereotype.Component;

import com.remitro.event.common.AggregateType;
import com.remitro.event.common.EventEnvelope;
import com.remitro.event.common.EventType;
import com.remitro.event.domain.member.MemberLockedEvent;
import com.remitro.event.domain.member.MemberSignedUpEvent;
import com.remitro.event.domain.member.MemberUnlockedEvent;
import com.remitro.event.domain.member.MemberWithdrawnEvent;
import com.remitro.member.application.event.context.EventContextProvider;
import com.remitro.member.application.event.factory.MemberEventFactory;
import com.remitro.member.domain.member.model.Member;
import com.remitro.member.infrastructure.messaging.EventProducer;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MemberEventPublisher {

	private final EventProducer eventProducer;
	private final EventContextProvider eventContextProvider;

	public void publishMemberSignedUp(Member member) {
		final MemberSignedUpEvent signedUpEventPayload = MemberEventFactory.createSignedUpEvent(member);
		publish(EventType.MEMBER_SIGNED_UP, member.getId().toString(), signedUpEventPayload);
	}

	public void publishMemberLocked(Member member) {
		final MemberLockedEvent lockedEventPayload = MemberEventFactory.createLockedEvent(member);
		publish(EventType.MEMBER_LOCKED, member.getId().toString(), lockedEventPayload);
	}

	public void publishMemberUnlocked(Member member) {
		final MemberUnlockedEvent unlockedEventPayload = MemberEventFactory.createUnlockedEvent(member);
		publish(EventType.MEMBER_UNLOCKED, member.getId().toString(), unlockedEventPayload);
	}

	public void publishMemberWithdrawn(Member member) {
		final MemberWithdrawnEvent withdrawnEventPayload = MemberEventFactory.createWithdrawnEvent(member);
		publish(EventType.MEMBER_WITHDRAWN, member.getId().toString(), withdrawnEventPayload);
	}

	private <T> void publish(EventType eventType, String aggregateId, T payload) {
		final EventEnvelope<T> eventEnvelope = EventEnvelope.of(
			AggregateType.MEMBER,
			eventType,
			aggregateId,
			eventContextProvider.producer(),
			eventContextProvider.traceId(),
			payload
		);

		eventProducer.send(eventEnvelope);
	}
}
