package com.remitro.auth.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "remitro-member", path = "/internal/members")
public interface MemberCommandClient {

	@PostMapping("/{memberId}/login-success")
	void recordLoginSuccess(@PathVariable Long memberId);

	@PostMapping("/{memberId}/login-failure")
	void recordLoginFailure(@PathVariable Long memberId);
}
