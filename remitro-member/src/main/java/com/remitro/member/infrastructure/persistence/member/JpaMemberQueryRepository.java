package com.remitro.member.infrastructure.persistence.member;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.remitro.member.domain.member.model.Member;
import com.remitro.member.domain.member.repository.MemberQueryRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class JpaMemberQueryRepository implements MemberQueryRepository {

	private final SpringDataMemberRepository springDataMemberRepository;

	@Override
	public Optional<Member> findById(Long id) {
		return springDataMemberRepository.findById(id);
	}

	@Override
	public boolean existsByEmail(String email) {
		return springDataMemberRepository.existsByEmail(email);
	}

	@Override
	public boolean existsByNickname(String nickname) {
		return springDataMemberRepository.existsByNickname(nickname);
	}

	@Override
	public boolean existsByPhoneNumber(String phoneNumber) {
		return springDataMemberRepository.existsByPhoneNumber(phoneNumber);
	}
}
