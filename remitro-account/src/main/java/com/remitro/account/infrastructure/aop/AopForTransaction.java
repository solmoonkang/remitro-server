package com.remitro.account.infrastructure.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AopForTransaction {

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public Object proceed(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		log.info("[✅ LOGGER] 비즈니스 로직 수행을 위한 새로운 트랜잭션을 시작합니다.");

		try {
			Object result = proceedingJoinPoint.proceed();
			log.info("[✅ LOGGER] 비즈니스 로직 수행이 완료되었습니다. COMMIT을 준비합니다.");
			return result;

		} catch (Exception e) {
			log.error("[✅ LOGGER] 예외가 발생하여 ROLLBACK을 진행합니다. (에러 내용 = {})", e.getMessage());
			throw e;
		}

	}
}
