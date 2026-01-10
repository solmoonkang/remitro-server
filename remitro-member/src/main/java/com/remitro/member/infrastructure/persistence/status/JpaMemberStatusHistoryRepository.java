package com.remitro.member.infrastructure.persistence.status;

import org.springframework.stereotype.Repository;

import com.remitro.member.domain.status.model.MemberStatusHistory;
import com.remitro.member.domain.status.repository.MemberStatusHistoryRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class JpaMemberStatusHistoryRepository implements MemberStatusHistoryRepository {

	private final SpringDataMemberStatusHistoryRepository springDataMemberStatusHistoryRepository;

	@Override
	public void save(MemberStatusHistory memberStatusHistory) {
		springDataMemberStatusHistoryRepository.save(memberStatusHistory);
	}
}
