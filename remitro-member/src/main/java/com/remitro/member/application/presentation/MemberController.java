package com.remitro.member.application.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.remitro.member.application.dto.request.SignUpRequest;
import com.remitro.member.domain.service.MemberService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
@Tag(name = "사용자 APIs", description = "회원가입 및 사용자 정보 수정 API")
public class MemberController {

	private final MemberService memberService;

	@PostMapping("/signup")
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "사용자 회원가입", description = "사용자 회원가입을 진행합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "🎉 회원가입 성공"),
		@ApiResponse(responseCode = "400", description = "❌ 유효하지 않은 비밀번호"),
		@ApiResponse(responseCode = "404", description = "🔍 존재하지 않는 사용자"),
		@ApiResponse(responseCode = "409", description = "⚠️ 이미 존재하는 이메일 또는 닉네임"),
		@ApiResponse(responseCode = "500", description = "💥 서버 내부 오류")
	})
	public ResponseEntity<?> signUpMember(@Valid @RequestBody SignUpRequest signUpRequest) {
		memberService.signUpMember(signUpRequest);
		return ResponseEntity.ok().body("[✅ SUCCESS] 사용자 회원가입을 성공적으로 완료했습니다.");
	}
}
