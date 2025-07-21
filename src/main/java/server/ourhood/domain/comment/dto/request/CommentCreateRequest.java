package server.ourhood.domain.comment.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CommentCreateRequest(
	@NotBlank(message = "모먼트 ID는 비워둘 수 없습니다.")
	Long momentId,
	String commentContent,
	Long parentId
) {
}
