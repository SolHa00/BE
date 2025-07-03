package server.ourhood.domain.join.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import server.ourhood.domain.join.domain.JoinRequest;
import server.ourhood.domain.join.domain.JoinRequestStatus;
import server.ourhood.domain.room.domain.Room;
import server.ourhood.domain.user.domain.User;

@Repository
public interface JoinRequestRepository extends JpaRepository<JoinRequest, Long> {
	boolean existsByUserAndRoomAndStatus(User user, Room room, JoinRequestStatus status);
}
