package server.ourhood.domain.room.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import server.ourhood.domain.invitation.domain.Invitation;
import server.ourhood.global.annotation.DateFormat;

public record GetRoomInvitationResponse(
	List<RoomInvitation> invitationList
) {
	public record RoomInvitation(
		Long invitationId,
		String nickname,
		@DateFormat
		LocalDateTime createdAt
	) {
		public static RoomInvitation from(Invitation invitation) {
			return new RoomInvitation(
				invitation.getId(),
				invitation.getInvitee().getNickname(),
				invitation.getCreatedAt()
			);
		}
	}
}
