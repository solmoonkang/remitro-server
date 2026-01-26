package com.remitro.member.infrastructure.messaging;

import java.time.LocalDateTime;

public interface MessageSendSupport {

	void send(String email, String verificationCode, LocalDateTime now);
}
