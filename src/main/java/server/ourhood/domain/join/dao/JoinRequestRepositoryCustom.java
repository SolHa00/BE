package server.ourhood.domain.join.dao;

import java.util.List;

import server.ourhood.domain.join.domain.JoinRequest;
import server.ourhood.domain.join.domain.JoinRequestStatus;
import server.ourhood.domain.room.domain.Room;
import server.ourhood.domain.user.domain.User;

public interface JoinRequestRepositoryCustom {
	List<JoinRequest> findAllByRequesterAndStatusWithRoom(User user, JoinRequestStatus status);

	List<JoinRequest> findAllByRoomAndStatusWithRequester(Room room, JoinRequestStatus status);
}
