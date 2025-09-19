package com.remitro.account.domain.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.remitro.account.application.dto.request.CreatePublishedEventRequest;
import com.remitro.account.infrastructure.repository.PublishedEventRepository;
import com.remitro.common.common.entity.PublishedEvent;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PublishedEventService {

	private final PublishedEventRepository publishedEventRepository;

	@Transactional(propagation = Propagation.MANDATORY)
	public void recordPublishedEvent(CreatePublishedEventRequest createPublishedEventRequest) {
		final PublishedEvent publishedEvent = PublishedEvent.createPublishedEvent(createPublishedEventRequest.eventId(),
			createPublishedEventRequest.aggregateId(), createPublishedEventRequest.aggregateType(),
			createPublishedEventRequest.eventType(), createPublishedEventRequest.eventData());
		publishedEventRepository.save(publishedEvent);
	}
}
