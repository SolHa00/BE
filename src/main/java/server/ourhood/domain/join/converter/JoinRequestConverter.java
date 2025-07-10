package server.ourhood.domain.join.converter;

import server.ourhood.domain.join.domain.JoinRequest;
import server.ourhood.domain.room.domain.Room;
import server.ourhood.domain.user.domain.User;

public class JoinRequestConverter {
	public static JoinRequest toJoinRequest(User requester, Room room) {
		return JoinRequest.builder()
			.requester(requester)
			.room(room)
			.build();
	}
}
