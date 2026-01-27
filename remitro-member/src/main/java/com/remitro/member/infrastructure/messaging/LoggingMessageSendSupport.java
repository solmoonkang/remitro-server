package com.remitro.member.infrastructure.messaging;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class LoggingMessageSendSupport implements MessageSendSupport {

	@Override
	public void send(String email, String verificationCode, LocalDateTime now) {
		log.info("[✅ LOGGER] 전송 시간: {}, {}으로 인증 코드 {}를 전송했습니다.", now, email, verificationCode);
	}
}
