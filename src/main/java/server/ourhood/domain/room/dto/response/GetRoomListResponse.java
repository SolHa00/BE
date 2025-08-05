package server.ourhood.domain.room.dto.response;

import java.time.LocalDateTime;
import java.util.List;

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
		}

		public record RoomDetail(
			String roomName,
			String thumbnailUrl
		) {
		}
	}
}
