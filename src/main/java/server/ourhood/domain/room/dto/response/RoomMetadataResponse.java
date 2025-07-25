package server.ourhood.domain.room.dto.response;

import java.time.LocalDateTime;

import server.ourhood.domain.room.domain.Room;
import server.ourhood.global.annotation.DateFormat;

public record RoomMetadataResponse(
	Long roomId,
	String hostName,
	@DateFormat
	LocalDateTime createdAt
) {
	public static RoomMetadataResponse from(Room room) {
		return new RoomMetadataResponse(
			room.getId(),
			room.getHost().getNickname(),
			room.getCreatedAt()
		);
	}
}
