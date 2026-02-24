package com.remitro.web.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributedLock {

	String fromAccountId() default "";

	String toAccountId() default "";

	String waitTime() default "";

	String leaseTime() default "";
}
