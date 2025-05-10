package com.remitroserver.global.lock.core;

import java.util.Collections;

import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

@Component
public class RetryExecutor {

	private final RetryTemplate retryTemplate = createDefaultTemplate();

	public void execute(Runnable task) {
		retryTemplate.execute(context -> {
			task.run();
			return null;
		});
	}

	private RetryTemplate createDefaultTemplate() {
		RetryTemplate template = new RetryTemplate();

		SimpleRetryPolicy simpleRetryPolicy = new SimpleRetryPolicy(
			3, Collections.singletonMap(ObjectOptimisticLockingFailureException.class, true));

		FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
		fixedBackOffPolicy.setBackOffPeriod(200);

		template.setRetryPolicy(simpleRetryPolicy);
		template.setBackOffPolicy(fixedBackOffPolicy);

		return template;
	}
}
