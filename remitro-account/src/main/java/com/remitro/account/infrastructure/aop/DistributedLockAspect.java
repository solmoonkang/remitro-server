package com.remitro.account.infrastructure.aop;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringValueResolver;

import com.remitro.account.domain.account.repository.TransactionLockRepository;

import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
@Order(1)
public class DistributedLockAspect implements EmbeddedValueResolverAware {

	private final TransactionLockRepository transactionLockRepository;
	private final AopForTransaction aopForTransaction;

	private StringValueResolver stringValueResolver;

	public DistributedLockAspect(
		TransactionLockRepository transactionLockRepository,
		AopForTransaction aopForTransaction
	) {
		this.transactionLockRepository = transactionLockRepository;
		this.aopForTransaction = aopForTransaction;
	}

	@Override
	public void setEmbeddedValueResolver(@NotNull StringValueResolver stringValueResolver) {
		this.stringValueResolver = stringValueResolver;
	}

	@Around("@annotation(distributedLock)")
	public Object lock(ProceedingJoinPoint proceedingJoinPoint, DistributedLock distributedLock) throws Throwable {
		final MethodSignature methodSignature = (MethodSignature)proceedingJoinPoint.getSignature();

		final Long fromAccountId = parseAccountId(
			methodSignature,
			proceedingJoinPoint,
			distributedLock.fromAccountId()
		);
		final Long toAccountId = parseAccountId(
			methodSignature,
			proceedingJoinPoint,
			distributedLock.toAccountId()
		);

		final long waitTime = Long.parseLong(
			Objects.requireNonNull(stringValueResolver.resolveStringValue(distributedLock.waitTime()))
		);
		final long leaseTime = Long.parseLong(
			Objects.requireNonNull(stringValueResolver.resolveStringValue(distributedLock.leaseTime()))
		);

		final List<Long> lockTargetAccountIds = Stream.of(fromAccountId, toAccountId)
			.filter(Objects::nonNull)
			.distinct()
			.sorted()
			.toList();

		try {
			for (Long accountId : lockTargetAccountIds) {
				log.info("[✅ LOGGER] 계좌 ID {}에 대한 락 획득을 시도합니다. (대기 시간 = {}ms, 점유 시간 = {}ms)",
					accountId, waitTime, leaseTime
				);
				transactionLockRepository.acquireLock(accountId, waitTime, leaseTime);

				log.info("[✅ LOGGER] 계좌 ID {}에 대한 락 획득에 성공하였습니다.", accountId);
			}

			return aopForTransaction.proceed(proceedingJoinPoint);

		} finally {
			for (int index = lockTargetAccountIds.size() - 1; index >= 0; index--) {
				Long releaseTargetId = lockTargetAccountIds.get(index);
				transactionLockRepository.releaseLock(releaseTargetId);
				log.info("[✅ LOGGER] 계좌 ID {}에 대한 락을 해제하였습니다.", releaseTargetId);
			}
		}
	}

	private Long parseAccountId(
		MethodSignature methodSignature,
		ProceedingJoinPoint proceedingJoinPoint,
		String accountId
	) {
		return CustomSpringExpressionParser.getDynamicValue(
			methodSignature.getParameterNames(),
			proceedingJoinPoint.getArgs(),
			accountId
		);
	}
}
