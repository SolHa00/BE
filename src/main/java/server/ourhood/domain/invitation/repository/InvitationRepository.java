package server.ourhood.domain.invitation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import server.ourhood.domain.invitation.domain.Invitation;
import server.ourhood.domain.invitation.domain.InvitationStatus;
import server.ourhood.domain.room.domain.Room;
import server.ourhood.domain.user.domain.User;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, Long> {
	boolean existsByInviteeAndRoomAndStatus(User invitee, Room room, InvitationStatus status);

	List<Invitation> findByInviteeAndStatus(User user, InvitationStatus status);
}
