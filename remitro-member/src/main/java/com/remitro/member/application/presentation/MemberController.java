package com.remitro.member.application.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.remitro.common.auth.annotation.Auth;
import com.remitro.common.auth.model.AuthMember;
import com.remitro.member.application.dto.request.SignUpRequest;
import com.remitro.member.application.dto.request.UpdateMemberRequest;
import com.remitro.member.domain.service.MemberService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;

	@PostMapping("/signup")
	public ResponseEntity<?> signUpMember(@Valid @RequestBody SignUpRequest signUpRequest) {
		memberService.signUpMember(signUpRequest);
		return ResponseEntity.ok().body("[✅ SUCCESS] 사용자 회원가입을 성공적으로 완료했습니다.");
	}

	@PutMapping
	public ResponseEntity<?> updateMember(
		@Auth AuthMember authMember,
		@Valid @RequestBody UpdateMemberRequest updateMemberRequest) {

		memberService.updateMember(authMember, updateMemberRequest);
		return ResponseEntity.ok().body("[✅ SUCCESS] 사용자 정보 수정을 성공적으로 완료했습니다.");
	}
}
