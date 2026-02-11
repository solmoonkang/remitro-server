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

		final Long fromAccountId = CustomSpringExpressionParser.getDynamicValue(
			methodSignature.getParameterNames(),
			proceedingJoinPoint.getArgs(),
			distributedLock.fromAccountId()
		);

		final Long toAccountId = CustomSpringExpressionParser.getDynamicValue(
			methodSignature.getParameterNames(),
			proceedingJoinPoint.getArgs(),
			distributedLock.toAccountId()
		);

		final long waitTime = Long.parseLong(
			Objects.requireNonNull(stringValueResolver.resolveStringValue(distributedLock.waitTime()))
		);
		final long leaseTime = Long.parseLong(
			Objects.requireNonNull(stringValueResolver.resolveStringValue(distributedLock.leaseTime()))
		);

		final List<Long> lockTargetAccountIds = Stream.of(fromAccountId, toAccountId)
			.sorted()
			.toList();

		final Long firstLockAccountId = lockTargetAccountIds.get(0);
		final Long secondLockAccountId = lockTargetAccountIds.get(1);

		try {
			transactionLockRepository.acquireLock(firstLockAccountId, waitTime, leaseTime);
			transactionLockRepository.acquireLock(secondLockAccountId, waitTime, leaseTime);

			return aopForTransaction.proceed(proceedingJoinPoint);

		} finally {
			transactionLockRepository.releaseLock(secondLockAccountId);
			transactionLockRepository.releaseLock(firstLockAccountId);
		}
	}
}
