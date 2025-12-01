package com.remitro.account.application.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.account.application.service.account.DormantAccountService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class DormantAccountScheduler {

	private final DormantAccountService dormantAccountService;

	@Scheduled(cron = "0 0 3 * * *")
	@Transactional
	public void run() {
		int count = dormantAccountService.markDormantAccounts();
		log.info("[✅ LOGGER] DAILY 휴면 전환 처리를 완료했습니다: COUNT={}", count);
	}
}
