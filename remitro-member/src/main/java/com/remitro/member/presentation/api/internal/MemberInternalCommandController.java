package com.remitro.member.presentation.api.internal;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.remitro.member.application.command.MemberActivateDormantCommandService;
import com.remitro.member.application.command.MemberLoginFailureCommandService;
import com.remitro.member.application.command.MemberLoginSuccessCommandService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/internal/members")
@RequiredArgsConstructor
public class MemberInternalCommandController {

	private final MemberLoginSuccessCommandService memberLoginSuccessCommandService;
	private final MemberLoginFailureCommandService memberLoginFailureCommandService;
	private final MemberActivateDormantCommandService memberActivateDormantCommandService;

	@PostMapping("/{memberId}/login-success")
	public void loginSuccess(@PathVariable Long memberId) {
		memberLoginSuccessCommandService.handleLoginSuccess(memberId);
		memberActivateDormantCommandService.activate(memberId);
	}

	@PostMapping("/{memberId}/login-failure")
	public void loginFailure(@PathVariable Long memberId) {
		memberLoginFailureCommandService.handleLoginFailure(memberId);
	}
}
