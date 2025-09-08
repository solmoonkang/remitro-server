package com.remitro.member.domain.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.member.application.dto.request.SignUpRequest;
import com.remitro.member.application.validator.MemberValidator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

	private final MemberValidator memberValidator;
	private final MemberWriteService memberWriteService;

	@Transactional
	public void signUpMember(SignUpRequest signUpRequest) {
		memberValidator.validateEmailNotDuplicated(signUpRequest.email());
		memberValidator.validateNicknameNotDuplicated(signUpRequest.nickname());
		memberValidator.validatePasswordMatche(signUpRequest.password(), signUpRequest.checkPassword());
		memberWriteService.createMember(signUpRequest);
	}
}
