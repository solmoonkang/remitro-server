package com.remitro.member.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.remitro.member.domain.member.policy.MemberDataMaskingPolicy;
import com.remitro.member.domain.member.policy.MemberLoginPolicy;
import com.remitro.member.domain.member.policy.MemberPasswordPolicy;
import com.remitro.member.domain.member.policy.MemberSignUpPolicy;
import com.remitro.member.domain.member.policy.MemberUpdatePolicy;
import com.remitro.member.domain.member.repository.MemberRepository;
import com.remitro.member.domain.token.policy.RefreshTokenPolicy;

@Configuration
public class MemberPolicyConfig {

	@Bean
	public MemberSignUpPolicy memberSignUpPolicy(MemberRepository memberRepository) {
		return new MemberSignUpPolicy(memberRepository);
	}

	@Bean
	public MemberPasswordPolicy memberPasswordPolicy(PasswordEncoder passwordEncoder) {
		return new MemberPasswordPolicy(passwordEncoder);
	}

	@Bean
	public MemberDataMaskingPolicy memberDataMaskingPolicy() {
		return new MemberDataMaskingPolicy();
	}

	@Bean
	public MemberUpdatePolicy memberUpdatePolicy(MemberRepository memberRepository) {
		return new MemberUpdatePolicy(memberRepository);
	}

	@Bean
	public MemberLoginPolicy memberLoginPolicy() {
		return new MemberLoginPolicy();
	}

	@Bean
	public RefreshTokenPolicy refreshTokenPolicy() {
		return new RefreshTokenPolicy();
	}
}
