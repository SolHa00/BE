package server.ourhood.domain.room.dto.response;

import server.ourhood.domain.room.domain.Room;

public record RoomDetailResponse(
	String roomName,
	String roomDescription,
	String thumbnailUrl
) {
	public static RoomDetailResponse of(Room room, String thumbnailUrl) {
		return new RoomDetailResponse(
			room.getName(),
			room.getDescription(),
			thumbnailUrl
		);
	}
}
