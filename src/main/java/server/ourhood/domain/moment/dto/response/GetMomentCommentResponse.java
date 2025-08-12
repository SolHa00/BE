package server.ourhood.domain.moment.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import server.ourhood.global.annotation.DateFormat;

public record GetMomentCommentResponse(
	List<CommentInfo> comments
) {
	public record CommentInfo(
		Long parentId,
		Long commentId,
		String commentContent,
		Long userId,
		String nickname,
		@DateFormat
		LocalDateTime createdAt,
		List<CommentInfo> replyComments
	) {
		public static CommentInfo of(
			Long parentId,
			Long commentId,
			String commentContent,
			Long userId,
			String nickname,
			LocalDateTime createdAt,
			List<CommentInfo> replyComments) {
			return new CommentInfo(
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

}
