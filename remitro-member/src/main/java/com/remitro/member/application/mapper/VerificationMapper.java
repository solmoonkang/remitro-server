package com.remitro.member.application.mapper;

import com.remitro.member.application.command.dto.response.CodeConfirmResponse;
import com.remitro.member.application.command.dto.response.CodeIssueResponse;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VerificationMapper {

	public static CodeIssueResponse toCodeIssueResponse(String verificationCode	) {
		return new CodeIssueResponse(verificationCode);
	}

	public static CodeConfirmResponse toCodeConfirmResponse(String verificationToken) {
		return new CodeConfirmResponse(verificationToken);
	}
}
