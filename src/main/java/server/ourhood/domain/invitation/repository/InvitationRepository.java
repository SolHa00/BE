package server.ourhood.domain.invitation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import server.ourhood.domain.invitation.domain.Invitation;
import server.ourhood.domain.invitation.domain.InvitationStatus;
import server.ourhood.domain.room.domain.Room;
import server.ourhood.domain.user.domain.User;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, Long> {
	boolean existsByInviteeAndRoomAndStatus(User invitee, Room room, InvitationStatus status);

	@Query("SELECT i FROM Invitation i "
		+ "JOIN FETCH i.room r "
		+ "JOIN FETCH r.host "
		+ "WHERE i.invitee = :user AND i.status = :status")
	List<Invitation> findByInviteeAndStatusWithRoomAndHost(@Param("user") User user,
		@Param("status") InvitationStatus status);

	@Query("SELECT i FROM Invitation i JOIN FETCH i.invitee WHERE i.room = :room AND i.status = :status")
	List<Invitation> findByRoomAndStatusWithInvitee(@Param("room") Room room, @Param("status") InvitationStatus status);
}
