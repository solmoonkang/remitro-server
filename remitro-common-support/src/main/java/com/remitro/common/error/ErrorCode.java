package com.remitro.common.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

	// --- C (Common): 공통 에러 --- //
	INVALID_INPUT_VALUE(400, "C400001", "%s는 잘못된 입력 값입니다."),

	METHOD_NOT_ALLOWED(405, "C405001", "허용되지 않은 HTTP 메소드입니다."),

	INTERNAL_SERVER_ERROR(500, "C500001", "서버 내부 오류가 발생했습니다."),
	ALGORITHM_CRYPTO_ERROR(500, "C500002", "암호화 알고리즘 실행 중 오류가 발생했습니다."),

	// --- A (Auth): 인증 및 인가 --- //
	UNAUTHORIZED_ACCESS(401, "A401001", "인증이 필요합니다."),
	EXPIRED_TOKEN(401, "A401002", "만료된 토큰입니다."),

	INVALID_TOKEN(403, "A403001", "%s이(가) 유효하지 않은 토큰입니다."),

	// --- M (Member): 회원 계정 및 상태 --- //
	ALREADY_SUSPENDED(400, "M400001", "이미 정지 처리된 계정입니다."),
	ALREADY_WITHDRAWN(400, "M400002", "이미 탈퇴 처리된 계정입니다."),
	NOT_SUSPENDED(400, "M400003", "정지 상태가 아닌 계정은 해제할 수 없습니다."),
	INVALID_SUSPEND_UNTIL(400, "M400004", "정지 해제 예정 시각은 현재 시각보다 이후여야 합니다."),
	REJOIN_RESTRICTED_PERIOD(400, "M400005", "탈퇴 후 30일 이내에는 재가입이 불가능합니다."),

	MEMBER_LOCKED(403, "M403001", "로그인 5회 실패로 인해 잠긴 계정입니다."),
	MEMBER_DORMANT(403, "M403002", "장기 미접속으로 인한 휴면 계정입니다."),
	MEMBER_SUSPENDED(403, "M403003", "관리자에 의해 정지된 계정입니다."),
	MEMBER_WITHDRAWN(403, "M403004", "존재하지 않는 계정입니다."),

	MEMBER_NOT_FOUND(404, "M404001", "존재하지 않는 회원입니다."),

	DUPLICATE_EMAIL(409, "M409001", "%s는 이미 사용 중인 이메일입니다."),
	DUPLICATE_NICKNAME(409, "M409002", "%s는 이미 사용 중인 닉네임입니다."),
	DUPLICATE_PHONE_NUMBER(409, "M409003", "%s는 이미 등록된 전화번호입니다."),

	// --- P (Password): 비밀번호 및 보안 --- //
	WRONG_PASSWORD(400, "P400001", "현재 비밀번호가 일치하지 않습니다."),
	PASSWORD_REUSE_DENIED(400, "P400002", "현재 비밀번호와 동일한 비밀번호는 사용할 수 없습니다."),
	PASSWORD_CONFIRM_MISMATCH(400, "P400003", "새 비밀번호와 확인 비밀번호가 일치하지 않습니다."),

	INVALID_PASSWORD(401, "P401001", "잘못된 비밀번호입니다.");

	private final int status;
	private final String code;
	private final String message;

	public String formatted(Object... args) {
		return String.format(this.message, args);
	}
}

