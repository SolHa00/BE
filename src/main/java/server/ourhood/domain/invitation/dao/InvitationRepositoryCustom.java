package server.ourhood.domain.invitation.dao;

import java.util.List;

import server.ourhood.domain.invitation.domain.Invitation;
import server.ourhood.domain.invitation.domain.InvitationStatus;
import server.ourhood.domain.room.domain.Room;
import server.ourhood.domain.user.domain.User;

public interface InvitationRepositoryCustom {
	List<Invitation> findAllByInviteeAndStatusWithRoomAndHost(User user, InvitationStatus status);

	List<Invitation> findAllByRoomAndStatusWithInvitee(Room room, InvitationStatus status);
}
