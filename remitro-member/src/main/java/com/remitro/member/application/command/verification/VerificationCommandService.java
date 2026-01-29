package com.remitro.member.application.command.verification;

import java.time.Clock;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.member.application.command.dto.request.CodeSendRequest;
import com.remitro.member.application.command.dto.request.CodeVerifyRequest;
import com.remitro.member.application.command.dto.response.CodeConfirmResponse;
import com.remitro.member.application.command.dto.response.CodeIssueResponse;
import com.remitro.member.application.mapper.VerificationMapper;
import com.remitro.member.application.read.verification.VerificationFinder;
import com.remitro.member.application.support.VerificationCodeGenerator;
import com.remitro.member.domain.verification.model.Verification;
import com.remitro.member.domain.verification.repository.VerificationRepository;
import com.remitro.member.infrastructure.messaging.MessageSendSupport;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class VerificationCommandService {

	private static final long VERIFICATION_EXPIRE_MINUTES = 5;

	private final VerificationFinder verificationFinder;
	private final VerificationRepository verificationRepository;
	private final VerificationCodeGenerator verificationCodeGenerator;
	private final VerificationValidator verificationValidator;

	private final MessageSendSupport messageSendSupport;
	private final Clock clock;

	public CodeIssueResponse issueCode(CodeSendRequest codeSendRequest) {
		final LocalDateTime now = LocalDateTime.now(clock);

		final String verificationCode = verificationCodeGenerator.generate();
		final Verification verification = Verification.issue(
			codeSendRequest.email(),
			verificationCode,
			now.plusMinutes(VERIFICATION_EXPIRE_MINUTES)
		);
		verificationRepository.save(verification);

		messageSendSupport.send(codeSendRequest.email(), verificationCode, now);

		return VerificationMapper.toCodeIssueResponse(verificationCode);
	}

	public CodeConfirmResponse verifyCode(CodeVerifyRequest codeVerifyRequest) {
		final Verification verification = verificationFinder.getLatestPendingCode(codeVerifyRequest.email());

		verificationValidator.validateAlreadyConfirmed(verification);
		verificationValidator.validateCodeValidity(verification, codeVerifyRequest.verificationCode());

		verification.confirm();
		verificationRepository.save(verification);

		return VerificationMapper.toCodeConfirmResponse(verification.getVerificationToken());
	}
}
