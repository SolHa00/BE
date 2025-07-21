package server.ourhood.domain.image.dto.request;

import jakarta.validation.constraints.NotNull;
import server.ourhood.domain.image.domain.ImageFileExtension;

public record MomentImageCreateRequest(
	@NotNull(message = "이미지 파일의 확장자는 비워둘 수 없습니다.")
	ImageFileExtension imageFileExtension
) {
}
