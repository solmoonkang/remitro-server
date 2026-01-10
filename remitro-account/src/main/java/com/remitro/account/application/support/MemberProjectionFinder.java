package com.remitro.account.application.support;

import org.springframework.stereotype.Component;

import com.remitro.account.domain.projection.model.MemberProjection;
import com.remitro.account.domain.projection.repository.MemberProjectionRepository;
import com.remitro.common.error.code.ErrorCode;
import com.remitro.common.error.exception.NotFoundException;
import com.remitro.common.error.message.ErrorMessage;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MemberProjectionFinder {

	private final MemberProjectionRepository memberProjectionRepository;

	public MemberProjection getById(Long memberId) {
		return memberProjectionRepository.findById(memberId)
			.orElseThrow(() -> new NotFoundException(
				ErrorCode.MEMBER_NOT_FOUND, ErrorMessage.MEMBER_ID_NOT_FOUND, memberId
			));
	}
}
