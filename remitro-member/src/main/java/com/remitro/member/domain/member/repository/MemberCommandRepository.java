package com.remitro.member.domain.member.repository;

import com.remitro.member.domain.member.model.Member;

public interface MemberCommandRepository {

	Member save(Member member);
}
