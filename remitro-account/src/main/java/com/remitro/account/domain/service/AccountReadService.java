package com.remitro.account.domain.service;

import org.springframework.stereotype.Service;

import com.remitro.account.domain.model.read.MemberProjection;
import com.remitro.account.domain.repository.MemberProjectionRepository;
import com.remitro.common.infra.error.exception.NotFoundException;
import com.remitro.common.infra.error.model.ErrorMessage;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountReadService {

	private final MemberProjectionRepository memberProjectionRepository;

	public MemberProjection findMemberProjectionById(Long memberId) {
		return memberProjectionRepository.findById(memberId)
			.orElseThrow(() -> new NotFoundException(ErrorMessage.MEMBER_NOT_FOUND));
	}
}
