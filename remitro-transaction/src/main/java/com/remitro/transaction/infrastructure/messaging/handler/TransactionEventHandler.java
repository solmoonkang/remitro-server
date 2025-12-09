package com.remitro.transaction.infrastructure.messaging.handler;

public interface TransactionEventHandler<T> {

	void handle(String eventId, T event);
}
