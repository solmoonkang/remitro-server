package com.remitro.member.application.service.member;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.remitro.common.security.Role;
import com.remitro.member.application.dto.request.SignUpRequest;
import com.remitro.member.domain.enums.ActivityStatus;
import com.remitro.member.domain.enums.KycStatus;
import com.remitro.member.domain.model.Member;
import com.remitro.member.domain.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberWriteService {

	private final PasswordEncoder passwordEncoder;
	private final MemberRepository memberRepository;
	private final MemberEventPublisher memberEventPublisher;

	public void saveMember(SignUpRequest signUpRequest) {
		final Member member = saveMemberMetadata(signUpRequest);
		memberEventPublisher.publishCreated(member);
	}

	public void updateActivityStatus(Member member, ActivityStatus nextActivityStatus) {
		member.updateActivityStatus(nextActivityStatus);
		memberEventPublisher.publishActivityStatusUpdated(member);
	}

	public void updateKycStatus(Member member, Long adminMemberId, KycStatus nextKycStatus, String reason) {
		member.updateKycStatus(nextKycStatus);

		if (nextKycStatus == KycStatus.VERIFIED) {
			memberEventPublisher.publishKycVerified(member, adminMemberId);
			return;
		}

		memberEventPublisher.publishKycRejected(member, adminMemberId, reason);
	}

	public void requestKyc(Member member) {
		memberEventPublisher.publishKycRequested(member);
	}

	public void updateRole(Member member, Role nextRole, Long adminMemberId) {
		final Role previousRole = member.getRole();
		member.updateRole(nextRole);

		if (nextRole.isAdmin()) {
			memberEventPublisher.publishRoleGranted(member, previousRole, adminMemberId);
			return;
		}

		memberEventPublisher.publishRoleRevoked(member, previousRole, adminMemberId);
	}

	private Member saveMemberMetadata(SignUpRequest signUpRequest) {
		final Member member = Member.create(
			signUpRequest.email(),
			passwordEncoder.encode(signUpRequest.password()),
			signUpRequest.nickname(),
			signUpRequest.phoneNumber()
		);

		return memberRepository.save(member);
	}
}
