package server.ourhood.domain.room.dto.request;

import org.springframework.web.multipart.MultipartFile;

public record RoomUpdateRequest(
	String roomName,
	String roomDescription,
	MultipartFile thumbnail
) {
}
