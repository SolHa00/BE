package server.ourhood.domain.user.dto.response;

import java.time.LocalDateTime;

import server.ourhood.domain.join.domain.JoinRequest;
import server.ourhood.global.annotation.DateFormat;

public record SentJoinRequestResponse(
	Long joinRequestId,
	String roomName,
	@DateFormat
	LocalDateTime createdAt
) {
	public static SentJoinRequestResponse from(JoinRequest joinRequest) {
		return new SentJoinRequestResponse(
			joinRequest.getId(),
			joinRequest.getRoom().getName(),
			joinRequest.getCreatedAt()
		);
	}
}
