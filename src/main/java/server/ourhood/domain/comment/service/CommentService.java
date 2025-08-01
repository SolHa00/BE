package server.ourhood.domain.comment.service;

import static server.ourhood.global.exception.BaseResponseStatus.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import server.ourhood.domain.comment.domain.Comment;
import server.ourhood.domain.comment.dto.request.CommentCreateRequest;
import server.ourhood.domain.comment.dto.request.CommentUpdateRequest;
import server.ourhood.domain.comment.dto.response.CommentCreateResponse;
import server.ourhood.domain.comment.repository.CommentRepository;
import server.ourhood.domain.moment.domain.Moment;
import server.ourhood.domain.moment.service.MomentService;
import server.ourhood.domain.user.domain.User;
import server.ourhood.global.exception.BaseException;

@Service
@RequiredArgsConstructor
public class CommentService {

	private final CommentRepository commentRepository;
	private final MomentService momentService;

	public Comment getByCommentId(Long commentId) {
		return commentRepository.findById(commentId)
			.orElseThrow(() -> new BaseException(NOT_FOUND_COMMENT));
	}

	@Transactional
	public CommentCreateResponse createComment(User user, CommentCreateRequest request) {
		Moment moment = momentService.getByMomentId(request.momentId());
		Comment comment = createAndSaveComment(user, moment, request);
		return CommentCreateResponse.from(comment);
	}

	private Comment createAndSaveComment(User user, Moment moment, CommentCreateRequest request) {
		Comment comment = request.parentId() != null
			? Comment.createComment(request.commentContent(), moment, getByCommentId(request.parentId()), user)
			: Comment.createComment(request.commentContent(), moment, null, user);
		return commentRepository.save(comment);
	}

	@Transactional
	public void updateComment(User user, Long commentId, CommentUpdateRequest request) {
		Comment comment = getByCommentId(commentId);
		comment.validateOwner(user);
		comment.updateContent(request.commentContent());
	}

	@Transactional
	public void deleteComment(User user, Long commentId) {
		Comment comment = getByCommentId(commentId);
		comment.validateOwner(user);
		commentRepository.delete(comment);
	}
}
