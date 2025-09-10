package com.remitro.member.domain.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.common.auth.model.AuthMember;
import com.remitro.member.application.dto.request.SignUpRequest;
import com.remitro.member.application.dto.request.UpdateMemberRequest;
import com.remitro.member.application.validator.MemberValidator;
import com.remitro.member.domain.model.Member;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

	private final MemberValidator memberValidator;
	private final MemberWriteService memberWriteService;
	private final MemberReadService memberReadService;

	@Transactional
	public void signUpMember(SignUpRequest signUpRequest) {
		memberValidator.validateEmailNotDuplicated(signUpRequest.email());
		memberValidator.validateNicknameNotDuplicated(signUpRequest.nickname());
		memberValidator.validatePasswordMatche(signUpRequest.password(), signUpRequest.checkPassword());
		memberWriteService.saveMember(signUpRequest);
	}

	@Transactional
	public void updateMember(AuthMember authMember, UpdateMemberRequest updateMemberRequest) {
		final Member member = memberReadService.findMemberById(authMember.id());
		memberValidator.validateNicknameNotDuplicated(updateMemberRequest.nickname());
		memberWriteService.updateMember(member, updateMemberRequest);
	}
}
