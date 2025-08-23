package server.ourhood.domain.image.dto.response;

public record GetPresignedUrlResponse(
	String imageKey,
	String presignedUrl
) {
	public static GetPresignedUrlResponse of(String imageKey, String presignedUrl) {
		return new GetPresignedUrlResponse(imageKey, presignedUrl);
	}
}
