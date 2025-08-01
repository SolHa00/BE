package server.ourhood.domain.comment.dto.response;

import java.time.LocalDateTime;

import server.ourhood.domain.comment.domain.Comment;
import server.ourhood.global.annotation.DateFormat;

public record CommentCreateResponse(
	Long commentId,
	@DateFormat
	LocalDateTime createdAt
) {
	public static CommentCreateResponse from(Comment comment) {
		return new CommentCreateResponse(
			comment.getId(),
			comment.getCreatedAt()
		);
	}
}
