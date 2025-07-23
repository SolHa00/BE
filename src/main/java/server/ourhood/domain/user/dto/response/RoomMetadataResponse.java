package server.ourhood.domain.user.dto.response;

import java.time.LocalDateTime;

import server.ourhood.domain.room.domain.Room;
import server.ourhood.global.annotation.DateFormat;

public record RoomMetadataResponse(
	Long roomId,
	String hostName,
	int numOfMembers,
	@DateFormat
	LocalDateTime createdAt
) {
	public static RoomMetadataResponse of(Room room) {
		return new RoomMetadataResponse(
			room.getId(),
			room.getHost().getNickname(),
			room.getRoomMembers().size(),
			room.getCreatedAt()
		);
	}
}
