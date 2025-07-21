package server.ourhood.domain.join.dto.response;

public record JoinRequestCreateResponse(
	Long joinRequestId
) {
	public static JoinRequestCreateResponse of(Long joinRequestId) {
		return new JoinRequestCreateResponse(joinRequestId);
	}
}
