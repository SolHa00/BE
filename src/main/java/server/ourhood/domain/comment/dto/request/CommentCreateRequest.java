package server.ourhood.domain.comment.dto.request;

import server.ourhood.domain.comment.domain.Comment;
import server.ourhood.domain.moment.domain.Moment;
import server.ourhood.domain.user.domain.User;

public record CommentCreateRequest(
	Long momentId,
	String commentContent,
	Long parentId
) {
	public Comment toComment(User user, Moment moment, Comment parent) {
		return Comment.builder()
			.user(user)
			.moment(moment)
			.content(this.commentContent)
			.parent(parent)
			.build();
	}
}
