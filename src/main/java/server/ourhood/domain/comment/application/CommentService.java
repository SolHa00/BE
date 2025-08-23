package server.ourhood.domain.comment.application;

import static server.ourhood.global.exception.BaseResponseStatus.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import server.ourhood.domain.comment.dao.CommentRepository;
import server.ourhood.domain.comment.domain.Comment;
import server.ourhood.domain.comment.dto.request.CreateCommentRequest;
import server.ourhood.domain.comment.dto.request.UpdateCommentRequest;
import server.ourhood.domain.comment.dto.response.CreateCommentResponse;
import server.ourhood.domain.moment.application.MomentService;
import server.ourhood.domain.moment.domain.Moment;
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
	public CreateCommentResponse createComment(User user, CreateCommentRequest request) {
		Moment moment = momentService.getByMomentId(request.momentId());
		Comment comment = createAndSaveComment(user, moment, request);
		return CreateCommentResponse.from(comment);
	}

	private Comment createAndSaveComment(User user, Moment moment, CreateCommentRequest request) {
		Comment comment = request.parentId() != null
			? Comment.createComment(request.commentContent(), moment, getByCommentId(request.parentId()), user)
			: Comment.createComment(request.commentContent(), moment, null, user);
		return commentRepository.save(comment);
	}

	@Transactional
	public void updateComment(User user, Long commentId, UpdateCommentRequest request) {
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
