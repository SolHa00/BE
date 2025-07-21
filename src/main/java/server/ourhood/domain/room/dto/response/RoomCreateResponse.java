package server.ourhood.domain.room.dto.response;

public record RoomCreateResponse(
	Long roomId
) {
	public static RoomCreateResponse of(Long roomId) {
		return new RoomCreateResponse(roomId);
	}
}
