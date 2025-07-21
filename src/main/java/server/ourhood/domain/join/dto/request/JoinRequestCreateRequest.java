package server.ourhood.domain.join.dto.request;

import jakarta.validation.constraints.NotNull;

public record JoinRequestCreateRequest(
	@NotNull(message = "방 ID는 비워둘 수 없습니다.")
	Long roomId
) {
}
