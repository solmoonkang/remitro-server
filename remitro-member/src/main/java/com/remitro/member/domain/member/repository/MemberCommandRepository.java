package com.remitro.member.domain.member.repository;

import java.time.LocalDateTime;
import java.util.List;

import com.remitro.member.domain.member.model.ActivityStatus;
import com.remitro.member.domain.member.model.Member;

public interface MemberCommandRepository {

	Member save(Member member);

	List<Member> findActiveMembersInactiveSince(ActivityStatus activityStatus, LocalDateTime threshold);
}
