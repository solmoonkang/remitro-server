package com.remitro.member.presentation.internal;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.remitro.member.application.service.internal.InternalMemberAuthCommandService;
import com.remitro.member.application.service.internal.InternalMemberStatusCommandService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/members")
public class InternalMemberCommandController {

	private final InternalMemberAuthCommandService internalMemberAuthCommandService;
	private final InternalMemberStatusCommandService internalMemberStatusCommandService;

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
}
