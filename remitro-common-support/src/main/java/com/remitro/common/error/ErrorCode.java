package com.remitro.common.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

	/**
	 * C (Common): 공통 에러
	 */
	INVALID_INPUT_VALUE(400, "C101", "%s는 잘못된 입력 값입니다."),
	METHOD_NOT_ALLOWED(405, "C102", "허용되지 않은 HTTP 메소드입니다."),
	INTERNAL_SERVER_ERROR(500, "C103", "서버 내부 오류가 발생했습니다."),

	/**
	 * A (Auth): 인증/인가 관련
	 */
	UNAUTHORIZED_ACCESS(401, "A201", "인증이 필요합니다."),
	EXPIRED_TOKEN(401, "A202", "만료된 토큰입니다."),
	INVALID_TOKEN(403, "A203", "%s이(가) 유효하지 않은 토큰입니다."),

	/**
	 * M (Member): 회원 정보/프로필 관련
	 */
	MEMBER_NOT_FOUND(404, "M301", "존재하지 않는 회원입니다."),
	DUPLICATE_EMAIL(409, "M302", "%s는 이미 사용 중인 이메일입니다."),
	DUPLICATE_NICKNAME(409, "M303", "%s는 이미 사용 중인 닉네임입니다."),
	DUPLICATE_PHONE_NUMBER(409, "M304", "%s는 이미 등록된 전화번호입니다."),

	/**
	 * P (Password): 비밀번호/보안 관련
	 */
	INVALID_PASSWORD(401, "P401", "잘못된 비밀번호입니다."),
	WRONG_PASSWORD(400, "P402", "현재 비밀번호가 일치하지 않습니다."),
	PASSWORD_REUSE_DENIED(400, "P403", "현재 비밀번호와 동일한 비밀번호는 사용할 수 없습니다."),
	PASSWORD_CONFIRM_MISMATCH(400, "P404", "새 비밀번호와 확인 비밀번호가 일치하지 않습니다.");

	private final int status;
	private final String code;
	private final String message;

	public String formatted(Object... args) {
		return String.format(this.message, args);
	}
}
