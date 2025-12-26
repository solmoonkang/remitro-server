package com.remitro.member.presentation.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.remitro.common.presentation.ApiSuccessResponse;
import com.remitro.common.security.AuthenticatedUser;
import com.remitro.common.security.CurrentUser;
import com.remitro.member.application.dto.request.ChangePasswordRequest;
import com.remitro.member.application.dto.request.SignUpRequest;
import com.remitro.member.application.dto.request.UpdateMemberProfileRequest;
import com.remitro.member.application.dto.response.MemberInfoResponse;
import com.remitro.member.application.service.member.MemberPasswordCommandService;
import com.remitro.member.application.service.member.MemberProfileCommandService;
import com.remitro.member.application.service.member.MemberQueryService;
import com.remitro.member.application.service.member.MemberSignUpCommandService;

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

	private final MemberSignUpCommandService memberSignUpCommandService;
	private final MemberQueryService memberQueryService;
	private final MemberPasswordCommandService memberPasswordCommandService;
	private final MemberProfileCommandService memberProfileCommandService;

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
		memberSignUpCommandService.signUp(signUpRequest);
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
		return memberQueryService.getMemberInfo(authenticatedUser.memberId());
	}

	@PatchMapping("/me/password")
	@ResponseStatus(HttpStatus.OK)
	@Operation(
		summary = "비밀번호 변경",
		description = "로그인한 사용자의 비밀번호를 변경합니다."
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "🎉 비밀번호 변경 성공",
			content = @Content(schema = @Schema(implementation = ApiSuccessResponse.class))
		),
		@ApiResponse(responseCode = "400", description = "❌ 유효하지 않은 요청"),
		@ApiResponse(responseCode = "401", description = "🔒 인증되지 않은 사용자"),
		@ApiResponse(responseCode = "404", description = "🔍 존재하지 않는 사용자"),
		@ApiResponse(responseCode = "500", description = "💥 서버 내부 오류")
	})
	public ApiSuccessResponse changePassword(
		@CurrentUser AuthenticatedUser authenticatedUser,
		@Valid @RequestBody ChangePasswordRequest changePasswordRequest
	) {
		memberPasswordCommandService.changePassword(authenticatedUser.memberId(), changePasswordRequest);
		return ApiSuccessResponse.success("비밀번호가 성공적으로 변경되었습니다.");
	}

	@PatchMapping("/me/profile")
	@ResponseStatus(HttpStatus.OK)
	@Operation(
		summary = "프로필 정보 수정",
		description = "로그인한 사용자의 닉네임 및 전화번호를 수정합니다."
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "🎉 프로필 수정 성공",
			content = @Content(schema = @Schema(implementation = ApiSuccessResponse.class))
		),
		@ApiResponse(responseCode = "400", description = "❌ 유효하지 않은 요청"),
		@ApiResponse(responseCode = "401", description = "🔒 인증되지 않은 사용자"),
		@ApiResponse(responseCode = "404", description = "🔍 존재하지 않는 사용자"),
		@ApiResponse(responseCode = "409", description = "⚠️ 중복된 요청"),
		@ApiResponse(responseCode = "500", description = "💥 서버 내부 오류")
	})
	public ApiSuccessResponse updateProfile(
		@CurrentUser AuthenticatedUser authenticatedUser,
		@Valid @RequestBody UpdateMemberProfileRequest updateMemberProfileRequest
	) {
		memberProfileCommandService.updateProfile(authenticatedUser.memberId(), updateMemberProfileRequest);
		return ApiSuccessResponse.success("회원 정보가 성공적으로 수정되었습니다.");
	}
}
