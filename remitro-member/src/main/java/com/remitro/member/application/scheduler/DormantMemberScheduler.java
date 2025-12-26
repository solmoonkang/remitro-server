package com.remitro.member.application.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.remitro.member.application.service.member.DormantMemberService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DormantMemberScheduler {

	private final DormantMemberService dormantMemberService;

	@Scheduled(cron = "0 0 1 * * *")
	public void markDormantMembers() {
		dormantMemberService.convertDormantMembers();
	}
}
