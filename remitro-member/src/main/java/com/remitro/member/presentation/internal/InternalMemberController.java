package com.remitro.member.presentation.internal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.remitro.common.contract.MemberAuthInfo;
import com.remitro.member.application.service.internal.InternalMemberService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/members")
public class InternalMemberController {

	private final InternalMemberService internalMemberService;

	@GetMapping("/auth-info")
	public ResponseEntity<MemberAuthInfo> findAuthInfo(@RequestParam String email) {
		return ResponseEntity.ok().body(internalMemberService.findAuthInfo(email));
	}
}
