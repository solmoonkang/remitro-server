package com.remitro.member.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.remitro.member.domain.member.policy.MemberSignUpPolicy;
import com.remitro.member.domain.member.repository.MemberRepository;

@Configuration
public class MemberPolicyConfig {

	@Bean
	public MemberSignUpPolicy memberSignUpPolicy(MemberRepository memberRepository) {
		return new MemberSignUpPolicy(memberRepository);
	}
}
