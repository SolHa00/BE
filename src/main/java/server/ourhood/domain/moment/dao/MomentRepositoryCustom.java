package server.ourhood.domain.moment.dao;

import java.util.List;
import java.util.Optional;

import server.ourhood.domain.moment.domain.Moment;
import server.ourhood.domain.room.domain.Room;

public interface MomentRepositoryCustom {
	List<Moment> findAllByRoomWithImage(Room room);

	Optional<Moment> findByIdWithOwnerAndImage(Long momentId);
}
