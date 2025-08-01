package server.ourhood.domain.comment.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CommentUpdateRequest(
	@NotBlank(message = "댓글은 비워둘 수 없습니다.")
	String commentContent
) {
}
