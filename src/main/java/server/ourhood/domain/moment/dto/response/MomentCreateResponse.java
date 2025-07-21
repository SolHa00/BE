package server.ourhood.domain.moment.dto.response;

public record MomentCreateResponse(
	Long momentId
) {
	public static MomentCreateResponse of(Long momentId) {
		return new MomentCreateResponse(momentId);
	}
}
