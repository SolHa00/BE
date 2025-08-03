package server.ourhood.domain.invitation.dao;

import java.util.List;

import server.ourhood.domain.invitation.domain.Invitation;
import server.ourhood.domain.invitation.domain.InvitationStatus;
import server.ourhood.domain.room.domain.Room;
import server.ourhood.domain.user.domain.User;

public interface InvitationRepositoryCustom {
	List<Invitation> findByInviteeAndStatusWithRoomAndHost(User user, InvitationStatus status);

	List<Invitation> findByRoomAndStatusWithInvitee(Room room, InvitationStatus status);
}
