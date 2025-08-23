package server.ourhood.domain.invitation.api;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import server.ourhood.domain.invitation.api.docs.InvitationControllerDocs;
import server.ourhood.domain.invitation.application.InvitationService;
import server.ourhood.domain.invitation.dto.request.CreateInvitationRequest;
import server.ourhood.domain.invitation.dto.response.CreateInvitationResponse;
import server.ourhood.domain.user.domain.User;
import server.ourhood.global.auth.annotation.LoginUser;
import server.ourhood.global.response.BaseResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/invitations")
public class InvitationController implements InvitationControllerDocs {

	private final InvitationService invitationService;

	@PostMapping
	public BaseResponse<CreateInvitationResponse> createInvitation(@LoginUser User user,
		@Valid @RequestBody CreateInvitationRequest request) {
		return BaseResponse.success(invitationService.createInvitation(user, request));
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
