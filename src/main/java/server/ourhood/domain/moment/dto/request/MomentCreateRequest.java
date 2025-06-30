package server.ourhood.domain.moment.dto.request;

import org.springframework.web.multipart.MultipartFile;

public record MomentCreateRequest(
	Long roomId,
	MultipartFile momentImage,
	String momentDescription
) {
}
