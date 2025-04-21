package com.remitroserver.api.application.member;

import static com.remitroserver.global.error.model.ErrorMessage.*;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitroserver.api.application.member.mapper.MemberMapper;
import com.remitroserver.api.domain.auth.model.AuthMember;
import com.remitroserver.api.domain.member.entity.Member;
import com.remitroserver.api.domain.member.repository.MemberRepository;
import com.remitroserver.api.dto.member.request.SignUpRequest;
import com.remitroserver.api.dto.member.response.MemberInfoResponse;
import com.remitroserver.global.common.util.AES128Util;
import com.remitroserver.global.common.util.MaskingUtil;
import com.remitroserver.global.error.exception.BadRequestException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

	private final PasswordEncoder passwordEncoder;
	private final AES128Util aes128Util;
	private final MemberRepository memberRepository;
	private final MemberReadService memberReadService;

	@Transactional
	public void signUpMember(SignUpRequest signUpRequest) {
		memberReadService.validateEmailDuplicated(signUpRequest.email());
		memberReadService.validateNicknameDuplicated(signUpRequest.nickname());
		memberReadService.validateRegistrationNumberDuplicated(signUpRequest.registrationNumber());
		validatePasswordConfirmationMatch(signUpRequest.password(), signUpRequest.checkPassword());

		final String encodedPassword = passwordEncoder.encode(signUpRequest.password());
		final String encodedRegistrationNumber = aes128Util.encryptText(signUpRequest.registrationNumber());

		final Member member = Member.create(signUpRequest, encodedPassword, encodedRegistrationNumber);
		memberRepository.save(member);
	}

	public MemberInfoResponse getMemberInfo(AuthMember authMember) {
		final Member member = memberReadService.getMemberByEmail(authMember.email());
		final String decodedRegistrationNumber = aes128Util.decryptText(member.getRegistrationNumber());
		final String maskRegistrationNumber = MaskingUtil.maskRegistrationNumber(decodedRegistrationNumber);

		return MemberMapper.toInfoResponse(member, maskRegistrationNumber);
	}

	private void validatePasswordConfirmationMatch(String password, String checkPassword) {
		if (!password.equals(checkPassword)) {
			throw new BadRequestException(PASSWORD_MISMATCH_ERROR);
		}
	}
}
