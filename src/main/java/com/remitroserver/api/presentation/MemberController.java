package com.remitroserver.api.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.remitroserver.api.application.member.MemberService;
import com.remitroserver.api.domain.auth.model.AuthMember;
import com.remitroserver.api.dto.member.request.SignUpRequest;
import com.remitroserver.api.dto.member.response.MemberInfoResponse;
import com.remitroserver.global.auth.annotation.Auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
@Tag(name = "회원 APIs", description = "회원가입 및 회원 정보 관리 API")
public class MemberController {

	private final MemberService memberService;

	@PostMapping("/signup")
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(
		summary = "회원가입 - 사용자 계정 생성",
		description = "요청된 사용자 정보를 바탕으로 신규 회원 계정을 생성합니다."
	)
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "🎉 회원가입 완료"),
		@ApiResponse(responseCode = "400", description = "❌ 요청 데이터 형식 오류 또는 비밀번호 불일치"),
		@ApiResponse(responseCode = "409", description = "⚠️ 중복된 사용자 정보 존재"),
		@ApiResponse(responseCode = "500", description = "💥 서버 내부 오류 (예: AES 암호화 실패)")
	})
	public ResponseEntity<String> signUpMember(@RequestBody @Valid SignUpRequest signUpRequest) {
		memberService.signUpMember(signUpRequest);
		return ResponseEntity.ok().body("[✅ SUCCESS] 사용자 정보를 성공적으로 생성했습니다.");
	}

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	@Operation(
		summary = "내 정보 조회 - 인증된 사용자 정보 반환",
		description = "로그인된 사용자 본인의 닉네임, 마스킹된 주민등록번호, 가입일 정보를 반환합니다."
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "✅ 사용자 정보 조회 성공"),
		@ApiResponse(responseCode = "400", description = "❌ 주민등록번호 마스킹 실패 (잘못된 형식)"),
		@ApiResponse(responseCode = "404", description = "🔍 사용자를 찾을 수 없음"),
		@ApiResponse(responseCode = "500", description = "💥 서버 내부 오류 (AES 복호화 실패 등)")
	})
	public ResponseEntity<MemberInfoResponse> getMemberInfo(@Auth AuthMember authMember) {
		return ResponseEntity.ok().body(memberService.getMemberInfo(authMember));
	}
}
