package com.remitro.member.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.common.infra.auth.model.AuthMember;
import com.remitro.member.application.dto.request.SignUpRequest;
import com.remitro.member.application.dto.response.MemberInfoResponse;
import com.remitro.member.application.mapper.MemberMapper;
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
		memberValidator.validatePhoneNumberNotDuplicated(signUpRequest.phoneNumber());
		memberValidator.validatePasswordMatche(signUpRequest.password(), signUpRequest.checkPassword());
		memberWriteService.saveMember(signUpRequest);
	}

	public MemberInfoResponse findMemberInfo(AuthMember authMember) {
		final Member member = memberReadService.findMemberById(authMember.id());
		return MemberMapper.toMemberInfoResponse(member);
	}
}
