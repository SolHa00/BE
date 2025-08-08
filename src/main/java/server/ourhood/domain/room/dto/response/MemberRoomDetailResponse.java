package server.ourhood.domain.room.dto.response;

public record MemberRoomDetailResponse(
	UserContextResponse userContext,
	RoomMetadataResponse roomMetadata,
	RoomDetailResponse roomDetail,
	Long numOfNewJoinRequests
) implements GetRoomDetailResponse {
}
