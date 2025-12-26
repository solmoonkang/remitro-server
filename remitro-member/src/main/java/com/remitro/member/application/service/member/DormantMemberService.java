package com.remitro.member.application.service.member;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.member.domain.model.Member;
import com.remitro.member.domain.repository.MemberRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DormantMemberService {

	private static final int DORMANT_MONTHS = 12;
	private static final int DORMANT_MEMBER_FLUSH_SIZE = 500;

	private final MemberRepository memberRepository;
	private final Clock clock;

	@PersistenceContext
	private EntityManager entityManager;

	@Transactional
	public void convertDormantMembers() {
		LocalDateTime now = LocalDateTime.now(clock);
		LocalDateTime threshold = now.minusMonths(DORMANT_MONTHS);

		final List<Member> candidateMembers = memberRepository.findActiveMembersWithLastLoginBefore(threshold);

		int count = 0;
		for (Member member : candidateMembers) {
			member.markDormant();
			count++;

			if (count % DORMANT_MEMBER_FLUSH_SIZE == 0) {
				entityManager.flush();
				entityManager.clear();
			}
		}

		entityManager.flush();
		entityManager.clear();
	}
}
