package com.remitro.member.application.event;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.remitro.event.common.AggregateType;
import com.remitro.event.common.EventEnvelope;
import com.remitro.event.common.EventType;
import com.remitro.event.domain.kyc.KycApprovedEvent;
import com.remitro.event.domain.kyc.KycRejectedEvent;
import com.remitro.event.domain.kyc.KycRequestedEvent;
import com.remitro.member.domain.kyc.model.KycVerification;
import com.remitro.member.infrastructure.messaging.EventProducer;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class KycEventPublisher {

	private final EventProducer eventProducer;

	@Value("${spring.application.name}")
	private String producer;

	public void publishRequested(KycVerification kycVerification) {
		final KycRequestedEvent requestedEventPayload = KycEventFactory.createRequestedEvent(kycVerification);

		final EventEnvelope<KycRequestedEvent> requestedEnvelope = EventEnvelope.of(
			AggregateType.MEMBER,
			EventType.KYC_REQUESTED,
			kycVerification.getId().toString(),
			producer,
			TraceContext.get(),
			requestedEventPayload
		);

		eventProducer.send(requestedEnvelope);
	}

	public void publishApproved(KycVerification kycVerification) {
		final KycApprovedEvent approvedEventPayload = KycEventFactory.createApprovedEvent(kycVerification);

		final EventEnvelope<KycApprovedEvent> approvedEnvelope = EventEnvelope.of(
			AggregateType.MEMBER,
			EventType.KYC_APPROVED,
			kycVerification.getId().toString(),
			producer,
			TraceContext.get(),
			approvedEventPayload
		);

		eventProducer.send(approvedEnvelope);
	}

	public void publishRejected(KycVerification kycVerification) {
		final KycRejectedEvent rejectedEventPayload = KycEventFactory.createRejectedEvent(kycVerification);

		final EventEnvelope<KycRejectedEvent> rejectedEnvelope = EventEnvelope.of(
			AggregateType.MEMBER,
			EventType.KYC_REJECTED,
			kycVerification.getId().toString(),
			producer,
			TraceContext.get(),
			rejectedEventPayload
		);

		eventProducer.send(rejectedEnvelope);
	}
}
