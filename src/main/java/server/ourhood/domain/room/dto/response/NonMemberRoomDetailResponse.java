package server.ourhood.domain.room.dto.response;

public record NonMemberRoomDetailResponse(
	UserContextResponse userContext,
	RoomMetadataResponse roomMetadata,
	RoomDetailResponse roomDetail
) implements GetRoomDetailResponse {
}
