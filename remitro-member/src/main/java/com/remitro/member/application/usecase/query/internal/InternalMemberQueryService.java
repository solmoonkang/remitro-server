package com.remitro.member.application.usecase.query.internal;

import org.springframework.stereotype.Service;

import com.remitro.common.auth.MemberAuthInfo;
import com.remitro.common.error.code.ErrorCode;
import com.remitro.common.error.exception.NotFoundException;
import com.remitro.common.error.exception.UnauthorizedException;
import com.remitro.common.error.message.ErrorMessage;
import com.remitro.member.application.common.mapper.MemberAuthMapper;
import com.remitro.member.application.common.support.MemberFinder;
import com.remitro.member.domain.member.enums.ActivityStatus;
import com.remitro.member.domain.member.enums.AuthPurpose;
import com.remitro.member.domain.kyc.enums.KycStatus;
import com.remitro.member.domain.member.model.Member;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InternalMemberQueryService {

	private final MemberFinder memberFinder;

	public MemberAuthInfo findAuthInfo(String email, AuthPurpose authPurpose) {
		final Member member = memberFinder.getByEmail(email);
		validateAuthAllowed(member, authPurpose);
		return MemberAuthMapper.toMemberAuthInfo(member);
	}

	private void validateAuthAllowed(Member member, AuthPurpose authPurpose) {
		switch (authPurpose) {
			case LOGIN -> validateLoginAllowed(member);
			case REISSUE -> validateTokenReissueAllowed(member);
		}
	}

	private void validateLoginAllowed(Member member) {
		if (member.getActivityStatus() == ActivityStatus.WITHDRAWN) {
			throw new NotFoundException(
				ErrorCode.MEMBER_NOT_FOUND,
				ErrorMessage.MEMBER_NOT_FOUND
			);
		}

		if (member.getActivityStatus() == ActivityStatus.LOCKED) {
			throw new UnauthorizedException(
				ErrorCode.MEMBER_LOCKED_NOT_ALLOWED,
				ErrorMessage.MEMBER_LOCKED_NOT_ALLOWED
			);
		}

		if (member.getActivityStatus() == ActivityStatus.DORMANT) {
			throw new UnauthorizedException(
				ErrorCode.MEMBER_DORMANT_NOT_ALLOWED,
				ErrorMessage.MEMBER_DORMANT_NOT_ALLOWED
			);
		}

		if (member.getKycStatus() == KycStatus.REJECTED) {
			throw new UnauthorizedException(
				ErrorCode.KYC_NOT_ALLOWED_FOR_LOGIN,
				ErrorMessage.KYC_NOT_ALLOWED_FOR_LOGIN
			);
		}
	}

	private void validateTokenReissueAllowed(Member member) {
		if (member.getActivityStatus() != ActivityStatus.ACTIVE) {
			throw new UnauthorizedException(
				ErrorCode.AUTHENTICATION_NOT_ALLOWED,
				ErrorMessage.AUTHENTICATION_NOT_ALLOWED
			);
		}

		if (member.getKycStatus() != KycStatus.VERIFIED) {
			throw new UnauthorizedException(
				ErrorCode.KYC_NOT_ALLOWED_FOR_REISSUE,
				ErrorMessage.KYC_NOT_ALLOWED_FOR_REISSUE
			);
		}
	}
}
