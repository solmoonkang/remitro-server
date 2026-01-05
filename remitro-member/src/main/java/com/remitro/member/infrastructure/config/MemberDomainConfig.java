package com.remitro.member.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.remitro.member.domain.member.policy.MemberSignUpPolicy;
import com.remitro.member.domain.member.service.MemberSignUpDomainService;

@Configuration
public class MemberDomainConfig {

	@Bean
	public MemberSignUpDomainService memberSignUpDomainService(MemberSignUpPolicy memberSignUpPolicy) {
		return new MemberSignUpDomainService(memberSignUpPolicy);
	}

	@Bean
	public MemberSignUpPolicy memberSignUpPolicy() {
		return new MemberSignUpPolicy();
	}
}
