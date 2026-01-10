package com.remitro.account.application.command;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.account.application.support.MemberProjectionFinder;
import com.remitro.account.domain.account.enums.ProductType;
import com.remitro.account.domain.account.generator.PinNumberGenerator;
import com.remitro.account.domain.account.policy.AccountMemberPolicy;
import com.remitro.account.domain.account.policy.PinPolicy;
import com.remitro.account.domain.account.policy.VirtualAccountIssuePolicy;
import com.remitro.account.domain.account.service.AccountOpenDomainService;
import com.remitro.account.domain.projection.model.MemberProjection;
import com.remitro.account.presentation.dto.request.VirtualAccountIssueRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class VirtualAccountIssueCommandService {

	private final MemberProjectionFinder memberProjectionFinder;
	private final AccountOpenDomainService accountOpenDomainService;

	private final PinNumberGenerator pinNumberGenerator;
	private final VirtualAccountIssuePolicy virtualAccountIssuePolicy;
	private final AccountMemberPolicy accountMemberPolicy;
	private final PinPolicy pinPolicy;

	public void issue(Long memberId, VirtualAccountIssueRequest virtualAccountIssueRequest) {
		final MemberProjection memberProjection = memberProjectionFinder.getById(memberId);
		accountMemberPolicy.validateMember(memberProjection);

		virtualAccountIssuePolicy.validateIssue(virtualAccountIssueRequest.expiredAt());

		final String systemPinNumber = pinNumberGenerator.generate4Digits();
		accountOpenDomainService.open(
			memberProjection.getMemberId(),
			ProductType.VIRTUAL.getCategory().getDefaultAccountName(),
			pinPolicy.encode(systemPinNumber),
			ProductType.VIRTUAL
		);
	}
}
