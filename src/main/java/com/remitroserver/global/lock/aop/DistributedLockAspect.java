package com.remitroserver.global.lock.aop;

import static com.remitroserver.global.common.util.RedisConstant.*;
import static com.remitroserver.global.error.model.ErrorMessage.*;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import com.remitroserver.global.error.exception.ConflictException;

import lombok.RequiredArgsConstructor;

@Aspect
@Component
@RequiredArgsConstructor
public class DistributedLockAspect {

	private final RedissonClient redissonClient;

	@Around("@annotation(distributedLock)")
	public Object applyLock(ProceedingJoinPoint proceedingJoinPoint, DistributedLock distributedLock) throws Throwable {
		String keyExpression = distributedLock.key();

		EvaluationContext evaluationContext = new StandardEvaluationContext();
		String[] parameterNames = ((MethodSignature)proceedingJoinPoint.getSignature()).getParameterNames();
		Object[] args = proceedingJoinPoint.getArgs();

		for (int i = 0; i < parameterNames.length; i++) {
			evaluationContext.setVariable(parameterNames[i], args[i]);
		}

		ExpressionParser expressionParser = new SpelExpressionParser();
		String keyValue = expressionParser.parseExpression(keyExpression).getValue(evaluationContext, String.class);
		String lockKey = ACCOUNT_LOCK_KEY_PREFIX + keyValue;

		RLock lock = redissonClient.getLock(lockKey);
		boolean acquired = false;

		try {
			acquired = lock.tryLock(distributedLock.waitTime(), distributedLock.leaseTime(),
				distributedLock.timeUnit());
			if (!acquired) throw new ConflictException(ACCOUNT_CONCURRENCY_ERROR);
			return proceedingJoinPoint.proceed();
		} finally {
			if (acquired && lock.isHeldByCurrentThread()) lock.unlock();
		}
	}
}
