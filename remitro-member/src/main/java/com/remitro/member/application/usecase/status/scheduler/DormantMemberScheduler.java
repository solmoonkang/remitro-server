package com.remitro.member.application.usecase.status.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.remitro.member.application.usecase.status.service.DormantMemberBatchService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DormantMemberScheduler {

	private final DormantMemberBatchService dormantMemberBatchService;

	@Scheduled(cron = "0 0 1 * * *")
	public void runDormantMemberConversionJob() {
		dormantMemberBatchService.executeDormantMemberConversion();
	}
}
