package com.remitro.support.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

	// ============ COMMON ============ //
	INVALID_INPUT_VALUE(400, "C001", "%s(은)는 잘못된 입력 값입니다."),
	METHOD_NOT_ALLOWED(405, "C002", "허용되지 않은 요청 메서드입니다."),
	DUPLICATE_RESOURCE(409, "C003", "이미 존재하는 리소스이거나 중복된 데이터입니다."),
	DUPLICATE_REQUEST(409, "C004", "이미 처리 중이거나 완료된 요청입니다."),
	INTERNAL_SERVER_ERROR(500, "C005", "서버 내부 오류가 발생했습니다."),
	ALGORITHM_CRYPTO_ERROR(500, "C006", "데이터 처리 중 오류가 발생했습니다."),

	// ============ AUTH ============ //
	UNAUTHORIZED_ACCESS(401, "AU001", "인증이 필요한 서비스입니다."),
	EXPIRED_TOKEN(401, "AU002", "만료된 인증 토큰입니다."),
	INVALID_TOKEN(403, "AU003", "유효하지 않은 %s 토큰입니다."),

	// ============ MEMBER ============ //
	ALREADY_SUSPENDED(400, "M001", "이미 정지 처리된 계정입니다."),
	ALREADY_WITHDRAWN(400, "M002", "이미 탈퇴 처리된 계정입니다."),
	NOT_SUSPENDED(400, "M003", "정지 상태가 아닌 계정은 해제할 수 없습니다."),
	INVALID_SUSPEND_UNTIL(400, "M004", "정지 해제 예정 시각은 현재보다 이후여야 합니다."),
	REJOIN_RESTRICTED_PERIOD(400, "M005", "탈퇴 후 30일 이내에는 재가입이 불가능합니다."),
	MEMBER_LOCKED(403, "M006", "비밀번호 오류 횟수 초과로 잠긴 계정입니다."),
	MEMBER_DORMANT(403, "M007", "장기 미접속으로 인해 휴면 전환된 계정입니다."),
	MEMBER_SUSPENDED(403, "M008", "이용 정책 위반으로 정지된 계정입니다."),
	MEMBER_WITHDRAWN(403, "M009", "탈퇴 처리된 계정입니다."),
	MEMBER_NOT_FOUND(404, "M010", "존재하지 않는 회원 정보입니다."),
	DUPLICATE_EMAIL(409, "M011", "이미 사용 중인 이메일(%s)입니다."),
	DUPLICATE_NICKNAME(409, "M012", "이미 사용 중인 닉네임(%s)입니다."),
	DUPLICATE_PHONE_NUMBER(409, "M013", "이미 등록된 휴대폰 번호(%s)입니다."),
	MEMBER_INACTIVE(403, "M014", "활성화된 회원만 계좌를 개설할 수 있습니다."),

	// ============ PASSWORD ============ //
	WRONG_PASSWORD(400, "P001", "현재 비밀번호가 일치하지 않습니다."),
	PASSWORD_REUSE_DENIED(400, "P002", "현재와 동일한 비밀번호는 재사용할 수 없습니다."),
	PASSWORD_CONFIRM_MISMATCH(400, "P003", "비밀번호 확인이 일치하지 않습니다."),
	INVALID_PASSWORD(401, "P004", "비밀번호가 올바르지 않습니다."),

	// ============ VERIFICATION ============ //
	VERIFICATION_NOT_FOUND(404, "V001", "인증 정보를 찾을 수 없습니다."),
	VERIFICATION_EXPIRED(401, "V002", "인증 시간이 만료되었습니다."),
	VERIFICATION_CODE_MISMATCH(401, "V003", "인증 번호가 일치하지 않습니다."),
	VERIFICATION_ALREADY_CONFIRMED(409, "V004", "이미 완료된 인증 절차입니다."),

	// ============ ACCOUNT ============ //
	ACCOUNT_LIMIT_EXCEEDED(400, "AC001", "계좌 보유 한도를 초과하였습니다."),
	ACCOUNT_TYPE_NOT_OPENABLE(400, "AC002", "%s 계좌 타입은 개설이 허용되지 않습니다.");

	private final int status;
	private final String code;
	private final String message;

	public String formatted(Object... args) {
		return String.format(this.message, args);
	}
}
