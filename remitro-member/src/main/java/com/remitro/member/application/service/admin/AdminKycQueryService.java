package com.remitro.member.application.service.admin;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.member.application.dto.response.PendingKycResponse;
import com.remitro.member.application.support.KycVerificationFinder;
import com.remitro.member.application.support.MemberFinder;
import com.remitro.member.domain.model.KycVerification;
import com.remitro.member.domain.model.Member;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminKycQueryService {

	private final MemberFinder memberFinder;
	private final KycVerificationFinder kycVerificationFinder;

	public List<PendingKycResponse> findAllPendingKyc() {
		return kycVerificationFinder.findAllPending().stream()
			.map(this::toPendingKycResponse)
			.toList();
	}

	public PendingKycResponse findPendingKycByMember(Long memberId) {
		final KycVerification kycVerification = kycVerificationFinder.getLatestVerificationByMemberId(memberId);
		return toPendingKycResponse(kycVerification);
	}

	private PendingKycResponse toPendingKycResponse(KycVerification kycVerification) {
		final Member member = memberFinder.getById(kycVerification.getMemberId());

		return new PendingKycResponse(
			member.getId(),
			member.getEmail(),
			member.getNickname(),
			kycVerification.getRequestedAt()
		);
	}
}
