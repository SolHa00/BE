package server.ourhood.domain.user.dto.response;

import server.ourhood.domain.room.domain.Room;

public record RoomDetailResponse(
	String roomName,
	String thumbnail
) {
	public static RoomDetailResponse of(Room room, String thumbnailUrl) {
		return new RoomDetailResponse(
			room.getName(),
			thumbnailUrl
		);
	}
}
