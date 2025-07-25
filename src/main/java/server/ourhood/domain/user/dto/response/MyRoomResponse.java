package server.ourhood.domain.user.dto.response;

import java.time.LocalDateTime;

import server.ourhood.domain.room.domain.Room;
import server.ourhood.global.annotation.DateFormat;

public record MyRoomResponse(
	RoomMetadataResponse roomMetadata,
	RoomDetailResponse roomDetail
) {
	public static MyRoomResponse of(Room room, String thumbnailUrl) {
		return new MyRoomResponse(
			RoomMetadataResponse.from(room),
			RoomDetailResponse.of(room, thumbnailUrl)
		);
	}

	public record RoomMetadataResponse(
		Long roomId,
		String hostName,
		int numOfMembers,
		@DateFormat
		LocalDateTime createdAt
	) {
		public static RoomMetadataResponse from(Room room) {
			return new RoomMetadataResponse(
				room.getId(),
				room.getHost().getNickname(),
				room.getRoomMembers().size(),
				room.getCreatedAt()
			);
		}
	}

	public record RoomDetailResponse(
		String roomName,
		String thumbnailUrl
	) {
		public static RoomDetailResponse of(Room room, String thumbnailUrl) {
			return new RoomDetailResponse(
				room.getName(),
				thumbnailUrl
			);
		}
	}
}
