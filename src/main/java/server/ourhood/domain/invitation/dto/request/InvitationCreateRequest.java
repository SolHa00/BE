package server.ourhood.domain.invitation.dto.request;

import server.ourhood.domain.invitation.domain.Invitation;
import server.ourhood.domain.room.domain.Room;
import server.ourhood.domain.user.domain.User;

public record InvitationCreateRequest(
	Long roomId,
	String nickname
) {
	public Invitation toInvitation(User invitee, Room room) {
		return Invitation.builder()
			.invitee(invitee)
			.room(room)
			.build();
	}
}
