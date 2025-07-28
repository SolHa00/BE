package server.ourhood.domain.invitation.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import server.ourhood.domain.invitation.dto.request.InvitationCreateRequest;
import server.ourhood.domain.invitation.dto.response.InvitationCreateResponse;
import server.ourhood.domain.invitation.service.InvitationService;
import server.ourhood.domain.user.domain.User;
import server.ourhood.global.auth.annotation.LoginUser;
import server.ourhood.global.response.BaseResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/invitations")
public class InvitationController {

	private final InvitationService invitationService;

	@PostMapping
	public BaseResponse<InvitationCreateResponse> createInvitation(@LoginUser User user,
		@Valid @RequestBody InvitationCreateRequest request) {
		InvitationCreateResponse response = invitationService.createInvitation(user, request);
		return BaseResponse.success(response);
	}

	@PostMapping("/{invitationId}/accept")
	public BaseResponse<Void> acceptInvitation(@LoginUser User user, @PathVariable Long invitationId) {
		invitationService.accept(user, invitationId);
		return BaseResponse.success();
	}

	@PostMapping("/{invitationId}/reject")
	public BaseResponse<Void> rejectInvitation(@LoginUser User user, @PathVariable Long invitationId) {
		invitationService.reject(user, invitationId);
		return BaseResponse.success();
	}

	@PostMapping("/{invitationId}/cancel")
	public BaseResponse<Void> cancelInvitation(@LoginUser User user, @PathVariable Long invitationId) {
		invitationService.cancel(user, invitationId);
		return BaseResponse.success();
	}
}
