package server.ourhood.domain.invitation.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import server.ourhood.domain.invitation.dto.request.InvitationCreateRequest;
import server.ourhood.domain.invitation.dto.request.ProcessInvitationRequest;
import server.ourhood.domain.invitation.dto.response.InvitationCreateResponse;
import server.ourhood.domain.invitation.service.InvitationService;
import server.ourhood.domain.user.domain.User;
import server.ourhood.global.auth.annotation.LoginUser;
import server.ourhood.global.response.BaseResponse;

@RestController
@RequiredArgsConstructor
public class InvitationController {

	private final InvitationService invitationService;

	@PostMapping("/api/invitations")
	public BaseResponse<InvitationCreateResponse> addInvitation(@LoginUser User user,
		@RequestBody InvitationCreateRequest request) {
		InvitationCreateResponse response = invitationService.createInvitation(user, request);
		return BaseResponse.success(response);
	}

	@PutMapping("/api/invitations/{invitationId}")
	public BaseResponse<Void> processInvitation(@LoginUser User user, @PathVariable Long invitationId,
		@RequestBody ProcessInvitationRequest request) {
		invitationService.processInvitation(user, invitationId, request);
		return BaseResponse.success();
	}

	@DeleteMapping("/api/invitations/{invitationId}")
	public BaseResponse<Void> removeInvitation(@LoginUser User user, @PathVariable Long invitationId) {
		invitationService.deleteInvitation(user, invitationId);
		return BaseResponse.success();
	}
}
