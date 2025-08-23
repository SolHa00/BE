package server.ourhood.domain.room.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateRoomRequest(
	@NotBlank(message = "방 이름은 비워둘 수 없습니다.")
	String roomName,
	String roomDescription,
	Boolean isImageRemoved,
	String newThumbnailImageKey
) {
}
