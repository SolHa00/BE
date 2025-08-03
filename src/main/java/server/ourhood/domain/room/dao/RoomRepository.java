package server.ourhood.domain.room.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import server.ourhood.domain.room.domain.Room;
import server.ourhood.domain.user.domain.User;

public interface RoomRepository extends JpaRepository<Room, Long>, RoomRepositoryCustom {
	boolean existsByIdAndRoomMembersUser(Long id, User user);
}
