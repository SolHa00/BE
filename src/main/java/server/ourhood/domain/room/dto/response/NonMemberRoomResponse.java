package server.ourhood.domain.room.dto.response;

public record NonMemberRoomResponse(
	UserContextResponse userContext,
	RoomMetadataResponse roomMetadata,
	RoomDetailResponse roomDetail
) implements GetRoomResponse {
}
