package com.remitro.member.application.command;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.member.application.event.MemberEventPublisher;
import com.remitro.member.domain.member.repository.MemberCommandRepository;
import com.remitro.member.domain.member.repository.MemberQueryRepository;
import com.remitro.member.presentation.dto.request.SignUpRequest;
import com.remitro.member.domain.kyc.model.KycVerification;
import com.remitro.member.domain.kyc.repository.KycVerificationCommandRepository;
import com.remitro.member.domain.member.model.Member;
import com.remitro.member.domain.member.service.MemberSignUpDomainService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberSignUpCommandService {

	private final MemberQueryRepository memberQueryRepository;
	private final MemberCommandRepository memberCommandRepository;
	private final KycVerificationCommandRepository kycVerificationCommandRepository;

	private final MemberSignUpDomainService memberSignUpDomainService;
	private final MemberEventPublisher memberEventPublisher;

	private final PasswordEncoder passwordEncoder;

	@Transactional
	public void signUp(SignUpRequest signUpRequest) {
		final boolean emailExists = memberQueryRepository.existsByEmail(signUpRequest.email());
		final boolean nicknameExists = memberQueryRepository.existsByNickname(signUpRequest.nickname());
		final boolean phoneExists = memberQueryRepository.existsByPhoneNumber(signUpRequest.phoneNumber());

		final Member member = memberSignUpDomainService.signUp(
			signUpRequest.email(),
			passwordEncoder.encode(signUpRequest.password()),
			signUpRequest.nickname(),
			signUpRequest.phoneNumber(),
			emailExists,
			nicknameExists,
			phoneExists
		);
		memberCommandRepository.save(member);

		final KycVerification kycVerification = KycVerification.request(member.getId());
		kycVerificationCommandRepository.save(kycVerification);

		memberEventPublisher.publishMemberSignedUp(member);
	}
}
