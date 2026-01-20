package com.remitro.member.application.batch;

import java.time.Clock;
import java.time.LocalDateTime;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.member.domain.member.enums.MemberStatus;
import com.remitro.member.domain.member.model.Member;
import com.remitro.member.domain.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class DormancyCommandService {

	private static final String SORT_FIELD_LAST_LOGIN_AT = "lastLoginAt";

	private final MemberRepository memberRepository;
	private final DormancyBatchProperties dormancyBatchProperties;
	private final Clock clock;

	public int processInactivityStatusChange() {
		final LocalDateTime now = LocalDateTime.now(clock);

		final Slice<Member> dormancyCandidates = memberRepository.findByMemberStatusAndLastLoginAtBefore(
			MemberStatus.ACTIVE,
			now.minusYears(dormancyBatchProperties.inactivityYears()),
			PageRequest.of(0, dormancyBatchProperties.size(), Sort.by(SORT_FIELD_LAST_LOGIN_AT).ascending())
		);

		dormancyCandidates.forEach(member -> member.changeToDormant(now));

		return dormancyCandidates.getNumberOfElements();
	}
}
