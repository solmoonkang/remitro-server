package com.remitro.member.presentation.internal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.remitro.common.auth.MemberAuthInfo;
import com.remitro.member.application.service.internal.InternalMemberQueryService;
import com.remitro.member.domain.enums.AuthPurpose;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/members")
public class InternalMemberQueryController {

	private final InternalMemberQueryService internalMemberQueryService;

	@GetMapping("/auth-info/login")
	public MemberAuthInfo findLoginAuthInfo(@RequestParam String email) {
		return internalMemberQueryService.findAuthInfo(email, AuthPurpose.LOGIN);
	}

	@GetMapping("/auth-info/reissue")
	public MemberAuthInfo findReissueAuthInfo(@RequestParam String email) {
		return internalMemberQueryService.findAuthInfo(email, AuthPurpose.REISSUE);
	}
}
