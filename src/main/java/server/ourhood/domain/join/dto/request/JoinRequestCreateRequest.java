package server.ourhood.domain.join.dto.request;

import server.ourhood.domain.join.domain.JoinRequest;
import server.ourhood.domain.room.domain.Room;
import server.ourhood.domain.user.domain.User;

public record JoinRequestCreateRequest(
	Long roomId
) {
	public JoinRequest toJoinRequest(User requester, Room room) {
		return JoinRequest.builder()
			.requester(requester)
			.room(room)
			.build();
	}
}
