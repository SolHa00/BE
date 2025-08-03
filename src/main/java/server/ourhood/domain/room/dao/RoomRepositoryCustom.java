package server.ourhood.domain.room.dao;

import java.util.List;
import java.util.Optional;

import server.ourhood.domain.room.domain.Room;
import server.ourhood.domain.user.domain.User;

public interface RoomRepositoryCustom {
	List<Room> findAllByMemberWithDetails(User user);

	Optional<Room> findByIdWithAllDetails(Long roomId);

	Optional<Room> findByIdWithHostAndThumbnail(Long roomId);
}
