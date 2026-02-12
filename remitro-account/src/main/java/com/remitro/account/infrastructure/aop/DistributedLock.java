package com.remitro.account.infrastructure.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributedLock {

	String fromAccountId() default "";

	String toAccountId() default "";

	String waitTime() default "${distributed-lock.wait-time}";

	String leaseTime() default "${distributed-lock.lease-time}";
}
