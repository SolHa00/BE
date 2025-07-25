package server.ourhood.domain.room.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserContextResponse(
	Boolean isMember,
	Boolean isHost,
	Long sentJoinRequestId
) {
}
