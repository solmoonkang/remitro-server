package com.remitro.account.application.mapper;

import java.util.UUID;

import com.remitro.account.application.dto.request.CreatePublishedEventRequest;
import com.remitro.account.domain.model.Account;
import com.remitro.common.common.entity.enums.AggregateType;
import com.remitro.common.common.entity.enums.EventType;
import com.remitro.common.common.event.DepositEventMessage;
import com.remitro.common.common.event.TransferEventMessage;
import com.remitro.common.common.event.WithdrawEventMessage;
import com.remitro.common.common.util.JsonUtil;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EventMapper {

	public static <T> CreatePublishedEventRequest toCreatePublishedEventRequest(Account account, EventType eventType,
		T eventMessage) {

		return new CreatePublishedEventRequest(UUID.randomUUID().toString(), account.getId(), AggregateType.ACCOUNT,
			eventType, JsonUtil.toJSON(eventMessage));
	}

	public static DepositEventMessage toDepositEventMessage(String receiverAccountNumber, Long amount) {
		return new DepositEventMessage(receiverAccountNumber, amount);
	}

	public static WithdrawEventMessage toWithdrawEventMessage(String senderAccountNumber, Long amount) {
		return new WithdrawEventMessage(senderAccountNumber, amount);
	}

	public static TransferEventMessage toTransferEventMessage(String senderAccountNumber, String receiverAccountNumber,
		Long amount) {

		return new TransferEventMessage(senderAccountNumber, receiverAccountNumber, amount);
	}
}
