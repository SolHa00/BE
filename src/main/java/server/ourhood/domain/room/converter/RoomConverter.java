package server.ourhood.domain.room.converter;

import server.ourhood.domain.room.domain.Room;
import server.ourhood.domain.user.domain.User;

public class RoomConverter {
	public static Room toRoom(String roomName, String roomDescription, String thumbnailImageUrl, User host) {
		return Room.builder()
			.roomName(roomName)
			.roomDescription(roomDescription)
			.thumbnailImageUrl(thumbnailImageUrl)
			.host(host)
			.build();
	}
}
