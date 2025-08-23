package server.ourhood.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateUserInfoRequest(
	@NotBlank(message = "닉네임은 비어있을 수 없습니다.")
	String nickname
) {
}
