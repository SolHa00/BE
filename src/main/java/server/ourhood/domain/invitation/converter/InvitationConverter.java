package server.ourhood.domain.invitation.converter;

import server.ourhood.domain.invitation.domain.Invitation;
import server.ourhood.domain.room.domain.Room;
import server.ourhood.domain.user.domain.User;

public class InvitationConverter {
	public static Invitation toInvitation(User invitee, Room room) {
		return Invitation.builder()
			.invitee(invitee)
			.room(room)
			.build();
	}
}
