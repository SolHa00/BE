package server.ourhood.domain.invitation.dto.request;

import jakarta.validation.constraints.NotNull;

public record InvitationCreateRequest(
	@NotNull(message = "방 ID는 비워둘 수 없습니다.")
	Long roomId,
	String nickname
) {
}
