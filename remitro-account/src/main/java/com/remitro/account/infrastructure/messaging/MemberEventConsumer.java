package com.remitro.account.infrastructure.messaging;

import static com.remitro.common.infra.util.KafkaConstant.*;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.account.domain.model.MemberProjection;
import com.remitro.account.domain.repository.MemberProjectionRepository;
import com.remitro.common.contract.member.MemberStatusChangedEvent;
import com.remitro.common.infra.util.JsonUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class MemberEventConsumer {

	private final MemberProjectionRepository memberProjectionRepository;

	@KafkaListener(
		topics = MEMBER_EVENTS_TOPIC_NAME,
		groupId = ACCOUNT_CONSUMER_GROUP_ID
	)
	@Transactional
	public void handleMemberStatusChangedEvent(String eventMessage) {
		try {
			final MemberStatusChangedEvent memberStatusChangedEvent = JsonUtil.fromJSON(
				eventMessage,
				MemberStatusChangedEvent.class
			);

			final MemberProjection member = MemberProjection.create(
				memberStatusChangedEvent.memberId(),
				memberStatusChangedEvent.nickname(),
				memberStatusChangedEvent.activityStatus()
			);

			memberProjectionRepository.findById(memberStatusChangedEvent.memberId())
				.ifPresentOrElse(
					memberProjection -> memberProjection.update(memberStatusChangedEvent.activityStatus()),
					() -> memberProjectionRepository.save(member)
				);
		} catch (Exception e) {
			log.error("[✅ LOGGER] 사용자 상태 변경 이벤트를 처리하지 못했습니다. "
					+ "EVENT = {}",
				eventMessage,
				e
			);
			throw e;
		}
	}
}
