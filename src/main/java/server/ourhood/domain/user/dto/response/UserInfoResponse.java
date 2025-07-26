package server.ourhood.domain.user.dto.response;

import java.util.List;

public record UserInfoResponse(
	List<MyRoomResponse> myRooms,
	List<ReceivedInvitationResponse> receivedInvitations,
	List<SentJoinRequestResponse> sentJoinRequests
) {
}
