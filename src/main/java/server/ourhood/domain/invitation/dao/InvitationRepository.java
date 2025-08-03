package server.ourhood.domain.invitation.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import server.ourhood.domain.invitation.domain.Invitation;
import server.ourhood.domain.invitation.domain.InvitationStatus;
import server.ourhood.domain.room.domain.Room;
import server.ourhood.domain.user.domain.User;

public interface InvitationRepository extends JpaRepository<Invitation, Long>, InvitationRepositoryCustom {
	boolean existsByInviteeAndRoomAndStatus(User invitee, Room room, InvitationStatus status);
}
