package server.ourhood.domain.comment.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateCommentRequest(
	@NotBlank(message = "댓글은 비워둘 수 없습니다.")
	String commentContent
) {
}
