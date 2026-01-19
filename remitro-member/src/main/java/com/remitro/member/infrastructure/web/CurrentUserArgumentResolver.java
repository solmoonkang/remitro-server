package com.remitro.member.infrastructure.web;

import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.remitro.common.error.ErrorCode;
import com.remitro.common.exception.UnauthorizedException;
import com.remitro.common.security.AuthenticatedUser;
import com.remitro.common.security.CurrentUser;

public class CurrentUserArgumentResolver implements HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter methodParameter) {
		return methodParameter.hasParameterAnnotation(CurrentUser.class) &&
			methodParameter.getParameterType().equals(AuthenticatedUser.class);
	}

	@Override
	public Object resolveArgument(
		MethodParameter methodParameter,
		ModelAndViewContainer mvcContainer,
		NativeWebRequest nativeWebRequest,
		WebDataBinderFactory webDataBinderFactory
	) {
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication != null && authentication.getPrincipal() instanceof AuthenticatedUser authenticatedUser) {
			return authenticatedUser;
		}

		throw new UnauthorizedException(ErrorCode.INVALID_TOKEN, "인증 정보");
	}
}
