package server.ourhood.domain.comment.converter;

import server.ourhood.domain.comment.domain.Comment;
import server.ourhood.domain.moment.domain.Moment;
import server.ourhood.domain.user.domain.User;

public class CommentConverter {
	public static Comment toComment(User user, Moment moment, String commentContent, Comment parent) {
		return Comment.builder()
			.user(user)
			.moment(moment)
			.content(commentContent)
			.parent(parent)
			.build();
	}
}
