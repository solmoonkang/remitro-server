package com.remitro.account.domain.service;

import com.remitro.account.domain.model.Account;

public interface TransactionRecorder {

	void recordTransferTransaction(Account senderAccount, Account receiverAccount, Long amount);

	void recordDepositTransaction(Account receiverAccount, Long amount);

	void recordWithdrawalTransaction(Account senderAccount, Long amount);
}
