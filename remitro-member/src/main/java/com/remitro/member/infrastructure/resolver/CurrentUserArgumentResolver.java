package com.remitro.member.infrastructure.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.remitro.common.security.AuthenticatedUser;
import com.remitro.common.security.CurrentUser;
import com.remitro.common.security.Role;

@Component
public class CurrentUserArgumentResolver implements HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter methodParameter) {
		return methodParameter.hasParameterAnnotation(CurrentUser.class)
			&& methodParameter.getParameterType().equals(AuthenticatedUser.class);
	}

	@Override
	public Object resolveArgument(
		MethodParameter methodParameter,
		ModelAndViewContainer modelAndViewContainer,
		NativeWebRequest nativeWebRequest,
		WebDataBinderFactory webDataBinderFactory
	) {
		final String memberIdHeader = nativeWebRequest.getHeader("X-Member-Id");
		final String memberRoleHeader = nativeWebRequest.getHeader("X-Member-Role");
		return new AuthenticatedUser(Long.parseLong(memberIdHeader), Role.valueOf(memberRoleHeader));
	}
}
