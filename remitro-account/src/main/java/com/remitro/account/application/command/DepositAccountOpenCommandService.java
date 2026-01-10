package com.remitro.account.application.command;

import java.time.Clock;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.account.application.support.MemberProjectionFinder;
import com.remitro.account.domain.account.model.Account;
import com.remitro.account.domain.account.policy.AccountMemberPolicy;
import com.remitro.account.domain.account.policy.DepositAccountOpenPolicy;
import com.remitro.account.domain.account.repository.AccountQueryRepository;
import com.remitro.account.domain.account.service.AccountOpenDomainService;
import com.remitro.account.domain.projection.model.MemberProjection;
import com.remitro.account.domain.status.enums.AccountStatus;
import com.remitro.account.domain.status.enums.StatusChangeReason;
import com.remitro.account.domain.status.model.AccountStatusHistory;
import com.remitro.account.domain.status.repository.AccountStatusHistoryRepository;
import com.remitro.account.presentation.dto.request.DepositAccountOpenRequest;
import com.remitro.account.presentation.dto.response.AccountOpenResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class DepositAccountOpenCommandService {

	private final MemberProjectionFinder memberProjectionFinder;
	private final AccountStatusHistoryRepository accountStatusHistoryRepository;
	private final AccountQueryRepository accountQueryRepository;
	private final AccountOpenDomainService accountOpenDomainService;

	private final DepositAccountOpenPolicy depositAccountOpenPolicy;
	private final AccountMemberPolicy accountMemberPolicy;
	private final Clock clock;

	public AccountOpenResponse open(Long memberId, DepositAccountOpenRequest depositAccountOpenRequest) {
		final MemberProjection memberProjection = memberProjectionFinder.getById(memberId);
		accountMemberPolicy.validateMember(memberProjection);

		final int depositAccountCount = accountQueryRepository.countByMemberId(memberProjection.getMemberId());
		depositAccountOpenPolicy.validateOpen(depositAccountCount);

		final Account account = accountOpenDomainService.open(
			memberProjection.getMemberId(),
			depositAccountOpenRequest.accountName(),
			depositAccountOpenRequest.pin(),
			depositAccountOpenRequest.productType()
		);

		final AccountStatusHistory accountStatusHistory = AccountStatusHistory.record(
			account.getId(),
			null,
			AccountStatus.NORMAL,
			StatusChangeReason.USER_REQUEST,
			LocalDateTime.now(clock)
		);
		accountStatusHistoryRepository.save(accountStatusHistory);

		return new AccountOpenResponse(account.getAccountNumber());
	}
}
