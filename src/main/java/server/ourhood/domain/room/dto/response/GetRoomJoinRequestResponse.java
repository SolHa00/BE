package server.ourhood.domain.room.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import server.ourhood.domain.join.domain.JoinRequest;
import server.ourhood.global.annotation.DateFormat;

public record GetRoomJoinRequestResponse(
	List<RoomJoinRequest> joinRequestList
) {
	public record RoomJoinRequest(
		Long joinRequestId,
		String nickname,
		@DateFormat
		LocalDateTime createdAt
	) {
		public static RoomJoinRequest from(JoinRequest joinRequest) {
			return new RoomJoinRequest(
				joinRequest.getId(),
				joinRequest.getRequester().getNickname(),
				joinRequest.getCreatedAt()
			);
		}
	}
}
