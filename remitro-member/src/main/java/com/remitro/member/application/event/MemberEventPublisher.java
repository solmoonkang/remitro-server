package com.remitro.member.application.event;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.remitro.event.common.AggregateType;
import com.remitro.event.common.EventEnvelope;
import com.remitro.event.common.EventType;
import com.remitro.event.domain.member.MemberSignedUpEvent;
import com.remitro.member.domain.member.model.Member;
import com.remitro.member.infrastructure.messaging.EventProducer;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MemberEventPublisher {

	private final EventProducer eventProducer;

	@Value("${spring.application.name}")
	private String producer;

	public void publishMemberSignedUp(Member member) {
		final MemberSignedUpEvent signedUpEventPayload = MemberEventFactory.createSignedUpEvent(member);

		final EventEnvelope<MemberSignedUpEvent> signedUpEnvelope = EventEnvelope.of(
			AggregateType.MEMBER,
			EventType.MEMBER_SIGNED_UP,
			member.getId().toString(),
			producer,
			TraceContext.get(),
			signedUpEventPayload
		);

		eventProducer.send(signedUpEnvelope);
	}
}
