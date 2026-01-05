package com.remitro.member.infrastructure.persistence.member;

import org.springframework.stereotype.Repository;

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
}
