package com.remitro.member.application.service.member;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	public void signUp(SignUpRequest signUpRequest) {
		memberValidator.validateUniqueEmail(signUpRequest.email());
		memberValidator.validateUniqueNickname(signUpRequest.nickname());
		memberValidator.validateUniquePhoneNumber(signUpRequest.phoneNumber());
		memberValidator.validatePasswordMatches(signUpRequest.password(), signUpRequest.checkPassword());
		memberWriteService.register(signUpRequest);
	}

	public MemberInfoResponse getMemberInfo(Long memberId) {
		final Member member = memberReadService.findMemberById(memberId);
		return MemberMapper.toMemberInfoResponse(member);
	}
}
