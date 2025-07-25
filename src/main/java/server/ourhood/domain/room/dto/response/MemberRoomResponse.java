package server.ourhood.domain.room.dto.response;

public record MemberRoomResponse(
	UserContextResponse userContext,
	RoomMetadataResponse roomMetadata,
	RoomDetailResponse roomDetail,
	RoomPrivateResponse roomPrivate
) implements GetRoomResponse {
}
