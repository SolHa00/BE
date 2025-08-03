package server.ourhood.domain.join.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import server.ourhood.domain.join.domain.JoinRequest;
import server.ourhood.domain.join.domain.JoinRequestStatus;
import server.ourhood.domain.room.domain.Room;
import server.ourhood.domain.user.domain.User;

public interface JoinRequestRepository extends JpaRepository<JoinRequest, Long>, JoinRequestRepositoryCustom {
	boolean existsByRequesterAndRoomAndStatus(User requester, Room room, JoinRequestStatus status);

	Optional<JoinRequest> findByRoomAndRequester(Room room, User requester);

	Long countByRoomAndStatus(Room room, JoinRequestStatus status);
}
