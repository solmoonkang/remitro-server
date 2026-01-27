package com.remitro.member.application.batch.suspension;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.remitro.member.application.command.status.SuspensionCommandService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class SuspensionBatchScheduler {

	private final SuspensionCommandService suspensionCommandService;

	@Scheduled(cron = "${batch.suspension.cron}")
	public void executeSuspensionRelease() {
		log.info("[✅ LOGGER] 정지 기간 만료 계정 자동 해제 배치를 시작합니다.");

		final int processedCount = suspensionCommandService.processSuspensionStatusChange();

		log.info("[✅ LOGGER] 정지 기간 만료 계정 자동 해제 배치를 완료했습니다. (처리된 계정 수: {}명)", processedCount);
	}
}
