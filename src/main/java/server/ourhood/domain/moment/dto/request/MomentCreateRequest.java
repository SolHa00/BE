package server.ourhood.domain.moment.dto.request;

public record MomentCreateRequest(
	Long roomId,
	String momentDescription
) {
}
