package com.remitro.member.presentation.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.remitro.common.auth.MemberAuthInfo;
import com.remitro.member.application.usecase.auth.internal.InternalMemberAuthCommandService;
import com.remitro.member.application.usecase.query.internal.InternalMemberQueryService;
import com.remitro.member.application.usecase.status.internal.InternalMemberStatusCommandService;
import com.remitro.member.domain.member.enums.AuthPurpose;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/members")
public class InternalMemberController {

	private final InternalMemberAuthCommandService internalMemberAuthCommandService;
	private final InternalMemberStatusCommandService internalMemberStatusCommandService;
	private final InternalMemberQueryService internalMemberQueryService;

	@PostMapping("/{memberId}/login-success")
	public void recordLoginSuccess(@PathVariable Long memberId) {
		internalMemberAuthCommandService.recordLoginSuccess(memberId);
	}

	@PostMapping("/{memberId}/login-failure")
	public void recordLoginFailure(@PathVariable Long memberId) {
		internalMemberAuthCommandService.recordLoginFailure(memberId);
	}

	@PostMapping("/{memberId}/unlock/by-self")
	public void unlockBySelf(@PathVariable Long memberId) {
		internalMemberAuthCommandService.unlockBySelfVerification(memberId);
	}

	@PostMapping("/{memberId}/dormant")
	public void markDormant(@PathVariable Long memberId) {
		internalMemberStatusCommandService.markDormant(memberId);
	}

	@PostMapping("/{memberId}/activate")
	public void activateDormant(@PathVariable Long memberId) {
		internalMemberStatusCommandService.activateDormant(memberId);
	}

	@GetMapping("/auth-info/login")
	public MemberAuthInfo findLoginAuthInfo(@RequestParam String email) {
		return internalMemberQueryService.findAuthInfo(email, AuthPurpose.LOGIN);
	}

	@GetMapping("/auth-info/reissue")
	public MemberAuthInfo findReissueAuthInfo(@RequestParam String email) {
		return internalMemberQueryService.findAuthInfo(email, AuthPurpose.REISSUE);
	}
}
