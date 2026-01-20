package com.remitro.member.application.command;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
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

	private final MemberRepository memberRepository;
	private final Clock clock;

	public int processInactivityStatusChange() {
		final Slice<Member> dormancyCandidates = memberRepository.findByMemberStatusAndLastLoginAtBefore(
			MemberStatus.ACTIVE,
			LocalDateTime.now(clock).minusYears(1),
			PageRequest.of(0, 500)
		);

		final List<Member> members = dormancyCandidates.getContent();
		members.forEach(Member::changeToDormant);

		return members.size();
	}
}
