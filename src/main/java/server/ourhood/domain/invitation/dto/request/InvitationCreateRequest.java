package server.ourhood.domain.invitation.dto.request;

public record InvitationCreateRequest(
	Long roomId,
	String nickname
) {
}
