package com.remitro.member.application.usecase.status.service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.member.domain.member.model.Member;
import com.remitro.member.domain.member.repository.MemberRepository;
import com.remitro.member.infrastructure.messaging.MemberEventPublisher;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DormantMemberBatchService {

	private static final int DORMANT_MONTHS = 12;
	private static final int FLUSH_SIZE = 500;

	private final MemberRepository memberRepository;
	private final Clock clock;
	private final MemberEventPublisher memberEventPublisher;

	@PersistenceContext
	private EntityManager entityManager;

	@Transactional
	public void executeDormantMemberConversion() {
		LocalDateTime now = LocalDateTime.now(clock);
		LocalDateTime threshold = now.minusMonths(DORMANT_MONTHS);

		final List<Member> candidateMembers = memberRepository.findActiveMembersWithLastLoginBefore(threshold);

		int processed = 0;

		for (Member member : candidateMembers) {
			member.markDormant();

			memberEventPublisher.publishMemberDormant(member, now);

			processed++;

			if (processed % FLUSH_SIZE == 0) {
				entityManager.flush();
				entityManager.clear();
			}
		}

		entityManager.flush();
		entityManager.clear();
	}
}
