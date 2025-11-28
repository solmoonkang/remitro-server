package com.remitro.auth.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.remitro.common.contract.MemberAuthInfo;

@FeignClient(name = "remitro-member", path = "/internal/members")
public interface MemberFeignClient {

	@GetMapping("/auth-info")
	MemberAuthInfo findAuthInfo(@RequestParam String email);
}
