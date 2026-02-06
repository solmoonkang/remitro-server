package com.remitro.account.application.projection;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.account.domain.projection.enums.MemberProjectionStatus;
import com.remitro.account.domain.projection.model.MemberProjection;
import com.remitro.account.domain.projection.repository.MemberProjectionRepository;
import com.remitro.event.domain.member.model.MemberRegisteredEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberEventService {

	private final MemberProjectionRepository memberProjectionRepository;

	@Transactional
	public void handleMemberRegistered(MemberRegisteredEvent memberRegisteredEvent) {
		final MemberProjection member = MemberProjection.create(
			memberRegisteredEvent.memberId(),
			memberRegisteredEvent.nickname(),
			MemberProjectionStatus.valueOf(memberRegisteredEvent.memberStatus().name())
		);
		memberProjectionRepository.save(member);

		log.info("[✅ LOGGER] KAFKA CONSUMER 회원 프로젝션 저장을 완료했습니다. (MEMBER_ID = {}, NICKNAME = {})",
			memberRegisteredEvent.memberId(), memberRegisteredEvent.nickname()
		);
	}
}
