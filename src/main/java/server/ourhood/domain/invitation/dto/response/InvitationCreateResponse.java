package server.ourhood.domain.invitation.dto.response;

public record InvitationCreateResponse(
	Long invitationId
) {
	public static InvitationCreateResponse of(Long invitationId) {
		return new InvitationCreateResponse(invitationId);
	}
}
