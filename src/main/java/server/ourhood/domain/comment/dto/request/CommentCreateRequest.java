package server.ourhood.domain.comment.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CommentCreateRequest(
	@NotNull(message = "모먼트 ID는 비워둘 수 없습니다.")
	Long momentId,
	@NotBlank(message = "댓글은 비워둘 수 없습니다.")
	String commentContent,
	Long parentId
) {
}
