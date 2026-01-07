package com.remitro.member.application.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.remitro.member.application.batch.MemberDormantBatch;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MemberDormantScheduler {

	private final MemberDormantBatch memberDormantBatch;

	@Scheduled(cron = "0 0 3 * * *")
	public void runDormantBatch() {
		memberDormantBatch.markInactiveMembersAsDormant();
	}
}
