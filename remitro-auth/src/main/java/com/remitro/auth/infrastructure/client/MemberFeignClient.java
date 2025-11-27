package com.remitro.auth.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.remitro.common.contract.member.MemberCredentialsResponse;

@FeignClient(name = "remitro-member", path = "/internal/members")
public interface MemberFeignClient {

	@GetMapping("/auth-info")
	MemberCredentialsResponse findAuthInfo(@RequestParam String email);
}
