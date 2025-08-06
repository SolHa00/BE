package server.ourhood.domain.room.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import server.ourhood.domain.room.domain.Room;
import server.ourhood.global.annotation.DateFormat;

public record GetRoomListResponse(
	List<RoomList> roomList
) {
	public record RoomList(
		RoomMetadata roomMetadata,
		RoomDetail roomDetail
	) {
		public record RoomMetadata(
			Long roomId,
			String hostName,
			@DateFormat
			LocalDateTime createdAt,
			int numOfMembers
		) {
			public static RoomMetadata from(Room room) {
				return new RoomMetadata(
					room.getId(),
					room.getHost().getNickname(),
					room.getCreatedAt(),
					room.getRoomMembers().size()
				);
			}
		}

		public record RoomDetail(
			String roomName,
			String thumbnailUrl
		) {
			public static RoomDetail of(Room room, String thumbnailUrl) {
				return new RoomDetail(
					room.getName(),
					thumbnailUrl
				);
			}
		}
	}
}
