package server.ourhood.domain.room.dto.request;

import server.ourhood.domain.room.domain.Room;
import server.ourhood.domain.user.domain.User;

public record RoomCreateRequest(
	String roomName,
	String roomDescription
) {
	public Room toRoom(String thumbnailImageUrl, User host) {
		return Room.builder()
			.roomName(this.roomName)
			.roomDescription(this.roomDescription)
			.thumbnailImageUrl(thumbnailImageUrl)
			.host(host)
			.build();
	}
}
