package com.remitro.account.application.read;

import org.springframework.stereotype.Component;

import com.remitro.account.domain.projection.model.MemberProjection;
import com.remitro.account.domain.projection.repository.MemberProjectionRepository;
import com.remitro.support.error.ErrorCode;
import com.remitro.support.exception.NotFoundException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MemberFinder {

	private final MemberProjectionRepository memberProjectionRepository;

	public MemberProjection getMemberById(Long memberId) {
		return memberProjectionRepository.findByMemberId(memberId)
			.orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));
	}
}
