package com.remitro.member.infrastructure.persistence.member;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.remitro.member.domain.member.enums.ActivityStatus;
import com.remitro.member.domain.member.model.Member;
import com.remitro.member.domain.member.repository.MemberCommandRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class JpaMemberCommandRepository implements MemberCommandRepository {

	private final SpringDataMemberRepository springDataMemberRepository;

	@Override
	public Member save(Member member) {
		return springDataMemberRepository.save(member);
	}

	@Override
	public List<Member> findActiveMembersInactiveSince(ActivityStatus activityStatus, LocalDateTime threshold) {
		return springDataMemberRepository.findActiveMembersInactiveSince(activityStatus, threshold);
	}
}
