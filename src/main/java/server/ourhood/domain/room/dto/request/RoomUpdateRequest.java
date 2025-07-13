package server.ourhood.domain.room.dto.request;

public record RoomUpdateRequest(
	String roomName,
	String roomDescription,
	Boolean isImageRemoved
) {
}
