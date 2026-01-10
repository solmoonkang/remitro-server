package com.remitro.account.application.command;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.account.application.support.MemberProjectionFinder;
import com.remitro.account.domain.account.enums.ProductType;
import com.remitro.account.domain.account.generator.PinNumberGenerator;
import com.remitro.account.domain.account.policy.AccountMemberPolicy;
import com.remitro.account.domain.account.policy.LoanAccountCreatePolicy;
import com.remitro.account.domain.account.policy.PinPolicy;
import com.remitro.account.domain.account.service.AccountOpenDomainService;
import com.remitro.account.domain.projection.model.MemberProjection;
import com.remitro.account.presentation.dto.request.LoanAccountCreateRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class LoanAccountCreateCommandService {

	private final MemberProjectionFinder memberProjectionFinder;
	private final AccountOpenDomainService accountOpenDomainService;

	private final PinNumberGenerator pinNumberGenerator;
	private final LoanAccountCreatePolicy loanAccountCreatePolicy;
	private final AccountMemberPolicy accountMemberPolicy;
	private final PinPolicy pinPolicy;

	public void create(Long memberId, LoanAccountCreateRequest loanAccountCreateRequest) {
		final MemberProjection memberProjection = memberProjectionFinder.getById(memberId);
		accountMemberPolicy.validateMember(memberProjection);

		loanAccountCreatePolicy.validateCreate(loanAccountCreateRequest.loanApproved());

		final String systemPinNumber = pinNumberGenerator.generate4Digits();
		accountOpenDomainService.open(
			memberProjection.getMemberId(),
			ProductType.LOAN.getCategory().getDefaultAccountName(),
			pinPolicy.encode(systemPinNumber),
			ProductType.LOAN
		);
	}
}
