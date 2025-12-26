package com.remitro.auth.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.remitro.common.auth.MemberAuthInfo;

@FeignClient(name = "remitro-member", path = "/internal/members")
public interface MemberQueryClient {

	@GetMapping("/auth-info/login")
	MemberAuthInfo findLoginAuthInfo(@RequestParam String email);

	@GetMapping("/auth-info/reissue")
	MemberAuthInfo findReissueAuthInfo(@RequestParam String email);
}
