package com.remitro.member.infrastructure.messaging;

import com.remitro.event.common.EventEnvelope;

public interface EventProducer {

	<T> void send(EventEnvelope<T> event);
}
