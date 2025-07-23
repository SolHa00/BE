package server.ourhood.domain.user.dto.response;

import server.ourhood.domain.room.domain.Room;

public record MyRoomResponse(
	RoomMetadataResponse roomMetadata,
	RoomDetailResponse roomDetail
) {
	public static MyRoomResponse from(Room room, String thumbnailUrl) {
		return new MyRoomResponse(
			RoomMetadataResponse.of(room),
			RoomDetailResponse.of(room, thumbnailUrl)
		);
	}
}
