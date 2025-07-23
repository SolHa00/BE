package server.ourhood.domain.user.dto.response;

import java.time.LocalDateTime;

import server.ourhood.domain.invitation.domain.Invitation;
import server.ourhood.global.annotation.DateFormat;

public record ReceivedInvitationResponse(
	Long invitationId,
	@DateFormat
	LocalDateTime createdAt,
	String roomName,
	String hostName
) {
	public static ReceivedInvitationResponse from(Invitation invitation) {
		return new ReceivedInvitationResponse(
			invitation.getId(),
			invitation.getCreatedAt(),
			invitation.getRoom().getName(),
			invitation.getRoom().getHost().getNickname()
		);
	}
}
