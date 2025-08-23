package server.ourhood.domain.moment.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateMomentRequest(
	@NotNull(message = "방 ID는 비워둘 수 없습니다.")
	Long roomId,
	String momentDescription,
	@NotBlank(message = "모먼트 이미지 키는 비워둘 수 없습니다.")
	String momentImageKey
) {
}
