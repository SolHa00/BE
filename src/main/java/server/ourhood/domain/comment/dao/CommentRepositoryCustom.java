package server.ourhood.domain.comment.dao;

import java.util.List;

import server.ourhood.domain.comment.domain.Comment;

public interface CommentRepositoryCustom {
	List<Comment> findAllCommentsByMomentId(Long momentId);
}
