package server.ourhood.domain.room.dto.request;

public record RoomCreateRequest(
	String roomName,
	String roomDescription
) {
}
