package com.remitro.member.presentation.internal;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.remitro.member.application.service.internal.InternalMemberCommandService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/members")
public class InternalMemberCommandController {

	private final InternalMemberCommandService internalMemberCommandService;

	@PostMapping("/{memberId}/login-success")
	public void recordLoginSuccess(@PathVariable Long memberId) {
		internalMemberCommandService.recordLoginSuccess(memberId);
	}

	@PostMapping("/{memberId}/login-failure")
	public void recordLoginFailure(@PathVariable Long memberId) {
		internalMemberCommandService.recordLoginFailure(memberId);
	}

	@PostMapping("/{memberId}/unlock/by-self")
	public void unlockBySelf(@PathVariable Long memberId) {
		internalMemberCommandService.unlockBySelfVerification(memberId);
	}

	@PostMapping("/{memberId}/dormant")
	public void markDormant(@PathVariable Long memberId) {
		internalMemberCommandService.markDormant(memberId);
	}

	@PostMapping("/{memberId}/activate")
	public void activateDormant(@PathVariable Long memberId) {
		internalMemberCommandService.activateDormant(memberId);
	}
}
