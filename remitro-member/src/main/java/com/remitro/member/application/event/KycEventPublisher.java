package com.remitro.member.application.event;

import org.springframework.stereotype.Component;

import com.remitro.event.common.AggregateType;
import com.remitro.event.common.EventEnvelope;
import com.remitro.event.common.EventType;
import com.remitro.event.domain.kyc.KycApprovedEvent;
import com.remitro.event.domain.kyc.KycRejectedEvent;
import com.remitro.event.domain.kyc.KycRequestedEvent;
import com.remitro.member.application.event.context.EventContextProvider;
import com.remitro.member.application.event.factory.KycEventFactory;
import com.remitro.member.domain.kyc.model.KycVerification;
import com.remitro.member.infrastructure.messaging.EventProducer;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class KycEventPublisher {

	private final EventProducer eventProducer;
	private final EventContextProvider eventContextProvider;

	public void publishRequested(KycVerification kycVerification) {
		final KycRequestedEvent requestedEventPayload = KycEventFactory.createRequestedEvent(kycVerification);
		publish(EventType.KYC_REQUESTED, kycVerification.getId().toString(), requestedEventPayload);
	}

	public void publishApproved(KycVerification kycVerification) {
		final KycApprovedEvent approvedEventPayload = KycEventFactory.createApprovedEvent(kycVerification);
		publish(EventType.KYC_APPROVED, kycVerification.getId().toString(), approvedEventPayload);
	}

	public void publishRejected(KycVerification kycVerification) {
		final KycRejectedEvent rejectedEventPayload = KycEventFactory.createRejectedEvent(kycVerification);
		publish(EventType.KYC_REJECTED, kycVerification.getId().toString(), rejectedEventPayload);
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
