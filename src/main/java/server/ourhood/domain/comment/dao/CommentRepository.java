package server.ourhood.domain.comment.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import server.ourhood.domain.comment.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {
}
