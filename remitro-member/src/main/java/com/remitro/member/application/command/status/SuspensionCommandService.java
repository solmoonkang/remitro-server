package com.remitro.member.application.command.status;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.member.application.batch.suspension.SuspensionBatchProperties;
import com.remitro.member.application.support.MemberStatusRecorder;
import com.remitro.member.domain.member.enums.MemberStatus;
import com.remitro.member.domain.member.model.Member;
import com.remitro.member.domain.audit.model.StatusHistory;
import com.remitro.member.domain.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SuspensionCommandService {

	private static final String SORT_FIELD_SUSPEND_UNTIL = "suspendUntil";

	private final MemberRepository memberRepository;
	private final MemberStatusRecorder memberStatusRecorder;
	private final SuspensionBatchProperties suspensionBatchProperties;
	private final SuspensionReleaseProcessor suspensionReleaseProcessor;
	private final Clock clock;

	@Transactional
	public int processSuspensionStatusChange() {
		final LocalDateTime now = LocalDateTime.now(clock);

		final Slice<Member> suspensionCandidates = memberRepository.findExpiredSuspensionCandidates(
			MemberStatus.SUSPENDED,
			now,
			PageRequest.of(0, suspensionBatchProperties.chunkSize(), Sort.by(SORT_FIELD_SUSPEND_UNTIL).ascending())
		);

		final List<StatusHistory> suspensionHistories = suspensionCandidates.stream()
			.map(this::tryReleaseSuspension)
			.filter(Objects::nonNull)
			.toList();

		memberStatusRecorder.recordAll(suspensionHistories);

		return suspensionHistories.size();
	}

	public StatusHistory tryReleaseSuspension(Member member) {
		try {
			return suspensionReleaseProcessor.processRelease(member);

		} catch (ObjectOptimisticLockingFailureException e) {
			log.warn("[✅ LOGGER] 관리자 수동 작업과 충돌하여 정지 해제 배치를 건너뜁니다. (회원 ID: {})", member.getId());
			return null;
		}
	}
}
