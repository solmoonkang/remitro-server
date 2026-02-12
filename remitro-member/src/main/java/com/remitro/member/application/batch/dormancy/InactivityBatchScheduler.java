package com.remitro.member.application.batch.dormancy;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.remitro.member.application.command.lifecycle.DormancyCommandService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class InactivityBatchScheduler {

	private final DormancyCommandService dormancyCommandService;
	private final DormancyBatchProperties dormancyBatchProperties;

	@Scheduled(cron = "${batch.dormancy.cron}")
	public void executeDormancyTransition() {
		log.info("[✅ LOGGER] 장기 미접속자 휴면 전환 배치를 시작합니다. (처리 단위: {}건)", dormancyBatchProperties.chunkSize());

		final int processedCount = dormancyCommandService.processInactivityStatusChange();

		log.info("[✅ LOGGER] 장기 미접속자 휴면 전환 배치를 완료했습니다. (처리된 계정 수: {}명)", processedCount);
	}
}
