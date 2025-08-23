package server.ourhood.domain.comment.dto.response;

import java.time.LocalDateTime;

import server.ourhood.domain.comment.domain.Comment;
import server.ourhood.global.annotation.DateFormat;

public record CreateCommentResponse(
	Long commentId,
	@DateFormat
	LocalDateTime createdAt
) {
	public static CreateCommentResponse from(Comment comment) {
		return new CreateCommentResponse(
			comment.getId(),
			comment.getCreatedAt()
		);
	}
}
