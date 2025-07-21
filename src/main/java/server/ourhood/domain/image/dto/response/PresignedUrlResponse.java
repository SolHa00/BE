package server.ourhood.domain.image.dto.response;

public record PresignedUrlResponse(
	String imageKey,
	String presignedUrl
) {
	public static PresignedUrlResponse of(String imageKey, String presignedUrl) {
		return new PresignedUrlResponse(imageKey, presignedUrl);
	}
}
