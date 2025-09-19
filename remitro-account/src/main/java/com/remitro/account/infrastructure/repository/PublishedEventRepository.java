package com.remitro.account.infrastructure.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.remitro.common.common.entity.PublishedEvent;
import com.remitro.common.common.entity.enums.EventStatus;

public interface PublishedEventRepository extends JpaRepository<PublishedEvent, Long> {

	@Query("SELECT pe FROM PublishedEvent pe WHERE pe.eventStatus = :eventStatus ORDER BY pe.createdAt ASC LIMIT :limit")
	List<PublishedEvent> findPendingEvents(@Param("eventStatus") EventStatus eventStatus, @Param("limit") int limit);
}
