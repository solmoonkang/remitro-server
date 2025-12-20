package com.remitro.member.application.service.internal;

import org.springframework.stereotype.Service;

import com.remitro.common.auth.MemberAuthInfo;
import com.remitro.common.error.exception.NotFoundException;
import com.remitro.common.error.exception.UnauthorizedException;
import com.remitro.common.error.model.ErrorMessage;
import com.remitro.member.application.mapper.MemberAuthMapper;
import com.remitro.member.application.service.member.MemberReadService;
import com.remitro.member.domain.enums.ActivityStatus;
import com.remitro.member.domain.enums.KycStatus;
import com.remitro.member.domain.model.Member;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InternalMemberService {

	private final MemberReadService memberReadService;

	public MemberAuthInfo findAuthInfo(String email) {
		final Member member = memberReadService.findMemberByEmail(email);
		validateLoginAllowed(member);
		return MemberAuthMapper.toMemberAuthInfo(member);
	}

	private void validateLoginAllowed(Member member) {
		if (member.getActivityStatus() == ActivityStatus.WITHDRAWN) {
			throw new NotFoundException(ErrorMessage.MEMBER_NOT_FOUND);
		}

		if (member.getActivityStatus() == ActivityStatus.LOCKED) {
			throw new UnauthorizedException(ErrorMessage.MEMBER_LOCKED);
		}

		if (member.getKycStatus() != KycStatus.VERIFIED) {
			throw new UnauthorizedException(ErrorMessage.KYC_NOT_VERIFIED);
		}
	}
}
