package com.remitro.account.domain.service.transaction;

import org.springframework.stereotype.Service;

import com.remitro.account.application.dto.request.TransferFormRequest;
import com.remitro.account.application.validator.AccountValidator;
import com.remitro.account.domain.model.Account;
import com.remitro.account.domain.service.AccountReadService;
import com.remitro.account.domain.service.AccountWriteService;
import com.remitro.account.domain.service.support.IdempotencyKeyHandler;
import com.remitro.account.infrastructure.redis.DistributedLockManager;
import com.remitro.common.auth.model.AuthMember;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransferTransactionHandler {

	private final AccountValidator accountValidator;
	private final DistributedLockManager distributedLockManager;
	private final AccountReadService accountReadService;
	private final AccountWriteService accountWriteService;
	private final IdempotencyKeyHandler idempotencyKeyHandler;

	public void transferToAccount(AuthMember authMember, Long accountId, String idempotencyKey, TransferFormRequest transferFormRequest) {
		idempotencyKeyHandler.preventDuplicateRequestAndRecordKey(idempotencyKey);

		final Long receiverId = accountReadService.findAccountIdByNumber(transferFormRequest.receiverAccountNumber());
		final Long keyA = Math.min(accountId, receiverId);
		final Long keyB = Math.max(accountId, receiverId);

		distributedLockManager.executeProtectedMultiDistributedLock(keyA, keyB, () -> {
			final Account sender = accountReadService.findAccountById(accountId);
			final Account receiver = accountReadService.findAccountById(receiverId);

			accountValidator.validateAccountAccess(sender.getMember().getId(), authMember.id());
			accountValidator.validateAccountPasswordMatch(transferFormRequest.password(), sender.getPassword());
			accountValidator.validateSufficientBalance(sender.getBalance(), transferFormRequest.amount());
			accountValidator.validateSelfTransfer(sender.getId(), receiver.getId());

			accountWriteService.processTransfer(sender, receiver, transferFormRequest);
		});
	}
}
