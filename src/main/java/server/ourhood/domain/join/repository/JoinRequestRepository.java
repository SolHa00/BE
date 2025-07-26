package server.ourhood.domain.join.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import server.ourhood.domain.join.domain.JoinRequest;
import server.ourhood.domain.join.domain.JoinRequestStatus;
import server.ourhood.domain.room.domain.Room;
import server.ourhood.domain.user.domain.User;

@Repository
public interface JoinRequestRepository extends JpaRepository<JoinRequest, Long> {
	boolean existsByRequesterAndRoomAndStatus(User requester, Room room, JoinRequestStatus status);

	@Query("SELECT jr FROM JoinRequest jr "
		+ "JOIN FETCH jr.room r "
		+ "WHERE jr.requester = :user AND jr.status = :status")
	List<JoinRequest> findByRequesterAndStatusWithRoom(@Param("user") User user,
		@Param("status") JoinRequestStatus status);

	Optional<JoinRequest> findByRoomAndRequester(Room room, User requester);

	Long countByRoomAndStatus(Room room, JoinRequestStatus status);

	@Query("SELECT j FROM JoinRequest j "
		+ "JOIN FETCH j.requester "
		+ "WHERE j.room = :room AND j.status = :status")
	List<JoinRequest> findByRoomAndStatusWithRequester(@Param("room") Room room,
		@Param("status") JoinRequestStatus status);
}
