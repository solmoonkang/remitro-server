package com.remitro.member.application.command;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.common.error.ErrorCode;
import com.remitro.common.exception.NotFoundException;
import com.remitro.common.security.Role;
import com.remitro.member.application.command.dto.request.WithdrawalRequest;
import com.remitro.member.application.support.MemberStatusRecorder;
import com.remitro.member.application.validator.PasswordValidator;
import com.remitro.member.domain.member.enums.ChangeReason;
import com.remitro.member.domain.member.enums.MemberStatus;
import com.remitro.member.domain.member.model.Member;
import com.remitro.member.domain.member.policy.MaskingPolicy;
import com.remitro.member.domain.member.repository.MemberRepository;
import com.remitro.member.domain.token.repository.RefreshTokenRepository;
import com.remitro.member.infrastructure.web.CookieManager;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class WithdrawalCommandService {

	private static final String WITHDRAWN_EMAIL_DOMAIN = "@withdrawn.com";
	private static final String WITHDRAWN_NICKNAME_PREFIX = "탈퇴사용자_";

	private final MemberRepository memberRepository;
	private final RefreshTokenRepository refreshTokenRepository;
	private final CacheManager cacheManager;
	private final MaskingPolicy maskingPolicy;
	private final PasswordValidator passwordValidator;
	private final CookieManager cookieManager;
	private final MemberStatusRecorder memberStatusRecorder;
	private final Clock clock;

	public void withdraw(Long memberId, WithdrawalRequest withdrawalRequest, HttpServletResponse httpServletResponse) {
		final LocalDateTime now = LocalDateTime.now(clock);

		final Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

		passwordValidator.validatePasswordMatch(withdrawalRequest.password(), member.getPasswordHash());

		final MemberStatus previousStatus = member.getMemberStatus();

		final String maskedEmail = UUID.randomUUID() + WITHDRAWN_EMAIL_DOMAIN;
		final String maskedNickname = WITHDRAWN_NICKNAME_PREFIX + UUID.randomUUID().toString().substring(0, 8);
		final String maskedPhoneNumber = maskingPolicy.maskPhoneNumberForWithdrawal(member.getPhoneNumber());
		member.withdraw(maskedEmail, maskedNickname, maskedPhoneNumber, now);

		refreshTokenRepository.deleteByMemberId(memberId);
		cookieManager.deleteRefreshTokenCookie(httpServletResponse);

		memberStatusRecorder.recordIfChanged(
			member,
			previousStatus,
			ChangeReason.USER_WITHDRAWN_BY_SELF,
			Role.USER,
			memberId
		);

		evictCaches(memberId, member.getEmail());
	}

	private void evictCaches(Long memberId, String email) {
		Cache profileCache = cacheManager.getCache("memberProfile");
		if (profileCache != null) {
			profileCache.evict("ID:" + memberId);
		}

		Cache sessionCache = cacheManager.getCache("memberSession");
		if (sessionCache != null) {
			sessionCache.evict("EMAIL:" + email);
		}
	}
}
