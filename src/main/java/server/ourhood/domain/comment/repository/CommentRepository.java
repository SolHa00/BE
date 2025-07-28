package server.ourhood.domain.comment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import server.ourhood.domain.comment.domain.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

	@Query("select c from Comment c "
		+ "join fetch c.owner "
		+ "left join fetch c.replyComments "
		+ "left join fetch c.parent "
		+ "where c.moment.id = :momentId")
	List<Comment> findAllCommentsByMomentId(Long momentId);
}
