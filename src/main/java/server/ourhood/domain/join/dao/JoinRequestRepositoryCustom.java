package server.ourhood.domain.join.dao;

import java.util.List;

import server.ourhood.domain.join.domain.JoinRequest;
import server.ourhood.domain.join.domain.JoinRequestStatus;
import server.ourhood.domain.room.domain.Room;
import server.ourhood.domain.user.domain.User;

public interface JoinRequestRepositoryCustom {
	List<JoinRequest> findByRequesterAndStatusWithRoom(User user, JoinRequestStatus status);

	List<JoinRequest> findByRoomAndStatusWithRequester(Room room, JoinRequestStatus status);
}
