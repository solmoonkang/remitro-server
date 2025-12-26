package com.remitro.member.presentation.member;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.remitro.common.presentation.ApiSuccessResponse;
import com.remitro.common.security.AuthenticatedUser;
import com.remitro.common.security.CurrentUser;
import com.remitro.member.application.dto.request.SignUpRequest;
import com.remitro.member.application.dto.response.MemberInfoResponse;
import com.remitro.member.application.service.member.MemberService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
@Tag(name = "사용자 APIs", description = "회원가입 및 사용자 정보 관리 API")
public class MemberController {

	private final MemberService memberService;

	@PostMapping("/signup")
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(
		summary = "사용자 회원가입",
		description = "신규 사용자를 회원으로 등록합니다."
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "201",
			description = "🎉 회원가입 성공",
			content = @Content(schema = @Schema(implementation = ApiSuccessResponse.class))
		),
		@ApiResponse(responseCode = "400", description = "❌ 유효하지 않은 요청"),
		@ApiResponse(responseCode = "409", description = "⚠️ 이미 존재하는 이메일 또는 닉네임"),
		@ApiResponse(responseCode = "500", description = "💥 서버 내부 오류")
	})
	public ApiSuccessResponse signUpMember(@Valid @RequestBody SignUpRequest signUpRequest) {
		memberService.signUp(signUpRequest);
		return ApiSuccessResponse.success("회원가입이 성공적으로 완료되었습니다.");
	}

	@GetMapping("/me")
	@ResponseStatus(HttpStatus.OK)
	@Operation(
		summary = "내 정보 조회",
		description = "로그인한 사용자의 회원 정보를 조회합니다."
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "🎉 사용자 정보 조회 성공",
			content = @Content(schema = @Schema(implementation = MemberInfoResponse.class))
		),
		@ApiResponse(responseCode = "401", description = "🔒 인증되지 않은 사용자"),
		@ApiResponse(responseCode = "404", description = "🔍 존재하지 않는 사용자"),
		@ApiResponse(responseCode = "500", description = "💥 서버 내부 오류")
	})
	public MemberInfoResponse getMyInfo(@CurrentUser AuthenticatedUser authenticatedUser) {
		return memberService.getMemberInfo(authenticatedUser.memberId());
	}
}
