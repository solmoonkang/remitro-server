package com.remitro.member.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.remitro.common.response.CommonResponse;
import com.remitro.common.security.AuthenticatedUser;
import com.remitro.common.security.CurrentUser;
import com.remitro.member.application.command.ProfileCommandService;
import com.remitro.member.application.command.SignUpCommandService;
import com.remitro.member.application.command.dto.request.ProfileUpdateRequest;
import com.remitro.member.application.command.dto.request.SignUpRequest;
import com.remitro.member.application.query.ProfileQueryService;
import com.remitro.member.application.query.dto.response.MemberProfileResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
@Tag(name = "MEMBER", description = "회원 정보 관련 API")
public class MemberController {

	private final SignUpCommandService signUpCommandService;
	private final ProfileQueryService profileQueryService;
	private final ProfileCommandService profileCommandService;

	@Operation(
		summary = "회원가입",
		description = "신규 사용자를 등록합니다."
	)
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "회원가입 성공"),
		@ApiResponse(responseCode = "400", description = "요청 값 검증 실패"),
		@ApiResponse(responseCode = "409", description = "중복된 회원 정보")
	})
	@PostMapping("/signup")
	@ResponseStatus(HttpStatus.CREATED)
	public CommonResponse<Void> signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
		signUpCommandService.signUp(signUpRequest);
		return CommonResponse.successNoContent();
	}

	@Operation(
		summary = "내 프로필 정보 조회",
		description = "로그인한 사용자의 마스킹된 프로필 정보를 조회합니다."
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "프로필 정보 조회 성공"),
		@ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
		@ApiResponse(responseCode = "404", description = "존재하지 않는 사용자"),
		@ApiResponse(responseCode = "500", description = "서버 내부 오류")
	})
	@GetMapping("/me")
	@ResponseStatus(HttpStatus.OK)
	public CommonResponse<MemberProfileResponse> getMyProfile(
		@Parameter(hidden = true) @CurrentUser AuthenticatedUser authenticatedUser
	) {
		return CommonResponse.success(
			profileQueryService.getMyProfile(authenticatedUser.memberId())
		);
	}

	@Operation(
		summary = "내 프로필 정보 수정",
		description = "로그인한 사용자의 닉네임과 전화번호를 수정합니다."
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "프로필 수정 성공"),
		@ApiResponse(responseCode = "400", description = "중복된 닉네임 또는 전화번호 존재"),
		@ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
		@ApiResponse(responseCode = "404", description = "존재하지 않는 사용자"),
		@ApiResponse(responseCode = "500", description = "서버 내부 오류")
	})
	@PatchMapping("/me")
	@ResponseStatus(HttpStatus.OK)
	public CommonResponse<Void> updateProfile(
		@Parameter(hidden = true) @CurrentUser AuthenticatedUser authenticatedUser,
		@Valid @RequestBody ProfileUpdateRequest profileUpdateRequest
	) {
		profileCommandService.updateProfile(authenticatedUser.memberId(), profileUpdateRequest);
		return CommonResponse.successNoContent();
	}
}
