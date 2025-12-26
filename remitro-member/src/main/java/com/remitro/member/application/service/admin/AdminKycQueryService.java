package com.remitro.member.application.service.admin;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.member.application.dto.response.PendingKycResponse;
import com.remitro.member.application.mapper.MemberMapper;
import com.remitro.member.application.service.kyc.KycReadService;
import com.remitro.member.application.service.member.MemberReadService;
import com.remitro.member.domain.model.KycVerification;
import com.remitro.member.domain.model.Member;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminKycQueryService {

	private final MemberReadService memberReadService;
	private final KycReadService kycReadService;

	public List<PendingKycResponse> findAllPendingKyc() {
		return kycReadService.findAllPending().stream()
			.map(this::toPendingKycResponse)
			.toList();
	}

	public PendingKycResponse findPendingKycByMember(Long memberId) {
		final KycVerification kycVerification = kycReadService.findKycVerificationByMemberId(memberId);
		return toPendingKycResponse(kycVerification);
	}

	private PendingKycResponse toPendingKycResponse(KycVerification kycVerification) {
		final Member member = memberReadService.findMemberById(kycVerification.getMemberId());
		return new PendingKycResponse(
			member.getId(),
			member.getEmail(),
			member.getNickname(),
			kycVerification.getRequestedAt()
		);
	}
}
