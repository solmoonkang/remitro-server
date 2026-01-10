package com.remitro.member.presentation.api.internal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.remitro.common.auth.MemberAuthInfo;
import com.remitro.member.application.query.MemberAuthInfoQueryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/internal/members/auth-info")
@RequiredArgsConstructor
public class MemberInternalQueryController {

	private final MemberAuthInfoQueryService memberAuthInfoQueryService;

	@GetMapping("/login")
	public MemberAuthInfo getLoginInfo(@RequestParam String email) {
		return memberAuthInfoQueryService.findForLogin(email);
	}
}
