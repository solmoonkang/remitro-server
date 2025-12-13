package com.remitro.account.infrastructure.messaging;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.remitro.account.application.service.account.AccountService;
import com.remitro.account.domain.enums.AccountStatus;
import com.remitro.account.domain.enums.AccountStatusUpdateReason;
import com.remitro.account.domain.event.account.AccountStatusUpdateRequestedEvent;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AccountStatusEventConsumer {

	private final AccountService accountService;

	@KafkaListener(
		topics = "${topic.account-status-command}",
		groupId = "${spring.kafka.consumer.group-id}"
	)
	public void handleAccountStatusEvent(AccountStatusUpdateRequestedEvent accountStatusUpdateRequestedEvent) {
		accountService.enforceAccountStatusUpdate(
			accountStatusUpdateRequestedEvent.accountId(),
			AccountStatus.valueOf(accountStatusUpdateRequestedEvent.targetStatus()),
			AccountStatusUpdateReason.valueOf(accountStatusUpdateRequestedEvent.reasonCode())
		);
	}
}
