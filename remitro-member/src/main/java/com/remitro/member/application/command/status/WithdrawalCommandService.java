package com.remitro.member.application.command.status;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.event.common.EventType;
import com.remitro.member.application.command.account.validator.PasswordValidator;
import com.remitro.member.application.command.dto.request.WithdrawalRequest;
import com.remitro.member.application.mapper.EventMapper;
import com.remitro.member.application.outbox.OutboxEventRecorder;
import com.remitro.member.application.read.account.MemberFinder;
import com.remitro.member.application.support.MemberStatusRecorder;
import com.remitro.member.domain.audit.enums.ChangeReason;
import com.remitro.member.domain.member.enums.MemberStatus;
import com.remitro.member.domain.member.model.Member;
import com.remitro.member.domain.member.policy.MaskingPolicy;
import com.remitro.member.domain.token.repository.RefreshTokenRepository;
import com.remitro.member.infrastructure.web.CookieManager;
import com.remitro.support.security.Role;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class WithdrawalCommandService {

	private static final String WITHDRAWN_EMAIL_DOMAIN = "@withdrawn.com";
	private static final String WITHDRAWN_NICKNAME_PREFIX = "탈퇴사용자_";

	private final MemberFinder memberFinder;
	private final RefreshTokenRepository refreshTokenRepository;
	private final MaskingPolicy maskingPolicy;
	private final PasswordValidator passwordValidator;

	private final OutboxEventRecorder outboxEventRecorder;

	private final CookieManager cookieManager;
	private final CacheManager cacheManager;
	private final MemberStatusRecorder memberStatusRecorder;
	private final Clock clock;

	public void withdraw(Long memberId, WithdrawalRequest withdrawalRequest, HttpServletResponse httpServletResponse) {
		final LocalDateTime now = LocalDateTime.now(clock);
		final Member member = memberFinder.getMemberById(memberId);

		passwordValidator.validatePasswordMatch(withdrawalRequest.password(), member.getPasswordHash());

		processWithdrawal(member, now);

		clearUserSessionAndData(member, httpServletResponse);
	}

	private void processWithdrawal(Member member, LocalDateTime now) {
		final MemberStatus previousStatus = member.getMemberStatus();

		final String maskedEmail = UUID.randomUUID() + WITHDRAWN_EMAIL_DOMAIN;
		final String maskedNickname = WITHDRAWN_NICKNAME_PREFIX + UUID.randomUUID().toString().substring(0, 8);
		final String maskedPhoneNumber = maskingPolicy.maskPhoneNumberForWithdrawal(member.getPhoneNumber());

		member.withdraw(maskedEmail, maskedNickname, maskedPhoneNumber, now);

		outboxEventRecorder.record(
			EventType.MEMBER_WITHDRAWN,
			member.getId(),
			EventMapper.toMemberWithdrawnEvent(member, now)
		);

		memberStatusRecorder.recordManualAction(
			member, previousStatus, ChangeReason.USER_WITHDRAWN_BY_SELF, Role.USER, member.getId()
		);
	}

	private void clearUserSessionAndData(Member member, HttpServletResponse httpServletResponse) {
		refreshTokenRepository.deleteByMemberId(member.getId());
		cookieManager.deleteRefreshTokenCookie(httpServletResponse);
		evictCaches(member.getId(), member.getEmail());
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
