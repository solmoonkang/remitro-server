package com.remitro.web.aop;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.core.annotation.Order;
import org.springframework.util.StringValueResolver;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Order(1)
@RequiredArgsConstructor
public class DistributedLockAspect implements EmbeddedValueResolverAware {

	private final DistributedLockManager distributedLockManager;
	private final AopForTransaction aopForTransaction;

	private StringValueResolver stringValueResolver;

	@Value("${distributed-lock.wait-time}")
	private long defaultWaitTime;

	@Value("${distributed-lock.lease-time}")
	private long defaultLeaseTime;

	@Override
	public void setEmbeddedValueResolver(@NotNull StringValueResolver stringValueResolver) {
		this.stringValueResolver = stringValueResolver;
	}

	@Around("@annotation(distributedLock)")
	public Object lock(ProceedingJoinPoint proceedingJoinPoint, DistributedLock distributedLock) throws Throwable {
		final MethodSignature methodSignature = (MethodSignature)proceedingJoinPoint.getSignature();

		final Long fromId = parseId(methodSignature, proceedingJoinPoint, distributedLock.fromAccountId());
		final Long toId = parseId(methodSignature, proceedingJoinPoint, distributedLock.toAccountId());

		final long waitTime = getWaitTime(distributedLock);
		final long leaseTime = getLeaseTime(distributedLock);

		final List<Long> lockTargetAccountIds = Stream.of(fromId, toId)
			.filter(Objects::nonNull)
			.distinct()
			.sorted()
			.toList();

		try {
			lockTargetAccountIds.forEach(targetId -> distributedLockManager.acquireLock(targetId, waitTime, leaseTime));
			return aopForTransaction.proceed(proceedingJoinPoint);

		} finally {
			for (int index = lockTargetAccountIds.size() - 1; index >= 0; index--) {
				distributedLockManager.releaseLock(lockTargetAccountIds.get(index));
			}
		}
	}

	private long getWaitTime(DistributedLock distributedLock) {
		if (distributedLock.waitTime().isEmpty())
			return defaultWaitTime;

		return Long.parseLong(
			Objects.requireNonNull(stringValueResolver.resolveStringValue(distributedLock.waitTime()))
		);
	}

	private long getLeaseTime(DistributedLock distributedLock) {
		if (distributedLock.leaseTime().isEmpty())
			return defaultLeaseTime;

		return Long.parseLong(
			Objects.requireNonNull(stringValueResolver.resolveStringValue(distributedLock.leaseTime()))
		);
	}

	private Long parseId(MethodSignature methodSignature, ProceedingJoinPoint proceedingJoinPoint, String expression) {
		return CustomSpringExpressionParser.getDynamicValue(
			methodSignature.getParameterNames(),
			proceedingJoinPoint.getArgs(),
			expression
		);
	}
}
