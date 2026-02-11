package com.remitro.account.infrastructure.aop;

import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

public final class CustomSpringExpressionParser {

	public static Long getDynamicValue(String[] parameterNames, Object[] arguments, String expression) {
		final ExpressionParser expressionParser = new SpelExpressionParser();
		final StandardEvaluationContext standardEvaluationContext = new StandardEvaluationContext();

		for (int index = 0; index < parameterNames.length; index++) {
			standardEvaluationContext.setVariable(parameterNames[index], arguments[index]);
		}

		return expressionParser.parseExpression(expression)
			.getValue(standardEvaluationContext, Long.class);
	}
}
