package server.ourhood.domain.comment.dao;

import static server.ourhood.domain.comment.domain.QComment.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import server.ourhood.domain.comment.domain.Comment;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<Comment> findAllCommentsByMomentId(Long momentId) {
		return queryFactory
			.selectFrom(comment)
			.join(comment.owner).fetchJoin()
			.leftJoin(comment.replyComments).fetchJoin()
			.leftJoin(comment.parent).fetchJoin()
			.where(comment.moment.id.eq(momentId))
			.fetch();
	}
}
