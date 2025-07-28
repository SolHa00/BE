package server.ourhood.domain.comment.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import server.ourhood.global.annotation.DateFormat;

public record CommentInfoResponse(
	Long parentId,
	Long commentId,
	String commentContent,
	Long userId,
	String nickname,
	@DateFormat
	LocalDateTime createdAt,
	List<CommentInfoResponse> replyComments
) {
	public static CommentInfoResponse of(
		Long parentId,
		Long commentId,
		String commentContent,
		Long userId,
		String nickname,
		LocalDateTime createdAt,
		List<CommentInfoResponse> replyComments) {
		return new CommentInfoResponse(
			parentId,
			commentId,
			commentContent,
			userId,
			nickname,
			createdAt,
			replyComments
		);
	}
}
