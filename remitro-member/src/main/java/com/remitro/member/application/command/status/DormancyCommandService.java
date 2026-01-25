package com.remitro.member.application.command.status;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.member.application.batch.dormancy.DormancyBatchProperties;
import com.remitro.member.application.support.MemberStatusRecorder;
import com.remitro.member.domain.audit.enums.ChangeReason;
import com.remitro.member.domain.member.enums.MemberStatus;
import com.remitro.member.domain.member.model.Member;
import com.remitro.member.domain.audit.model.StatusHistory;
import com.remitro.member.domain.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class DormancyCommandService {

	private static final String SORT_FIELD_LAST_LOGIN_AT = "lastLoginAt";

	private final MemberRepository memberRepository;
	private final MemberStatusRecorder memberStatusRecorder;
	private final DormancyBatchProperties dormancyBatchProperties;
	private final Clock clock;

	public int processInactivityStatusChange() {
		final LocalDateTime now = LocalDateTime.now(clock);

		final Slice<Member> dormancyCandidates = memberRepository.findDormancyCandidates(
			MemberStatus.ACTIVE,
			now.minusYears(dormancyBatchProperties.inactivityYears()),
			PageRequest.of(0, dormancyBatchProperties.chunkSize(), Sort.by(SORT_FIELD_LAST_LOGIN_AT).ascending())
		);

		final List<StatusHistory> dormancyHistories = dormancyCandidates.stream()
			.map(member -> processDormancy(member, now))
			.toList();

		memberStatusRecorder.recordAll(dormancyHistories);

		return dormancyCandidates.getNumberOfElements();
	}

	private StatusHistory processDormancy(Member member, LocalDateTime now) {
		final MemberStatus previousStatus = member.getMemberStatus();
		member.changeToDormant(now);

		return StatusHistory.ofSystem(member, previousStatus, ChangeReason.SYSTEM_DORMANT_BY_INACTIVITY);
	}
}
