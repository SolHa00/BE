package server.ourhood.domain.comment.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import server.ourhood.domain.comment.converter.CommentConverter;
import server.ourhood.domain.comment.domain.Comment;
import server.ourhood.domain.comment.dto.request.CommentCreateRequest;
import server.ourhood.domain.comment.dto.request.CommentUpdateRequest;
import server.ourhood.domain.comment.dto.response.CommentCreateResponse;
import server.ourhood.domain.comment.repository.CommentRepository;
import server.ourhood.domain.moment.domain.Moment;
import server.ourhood.domain.moment.service.MomentService;
import server.ourhood.domain.user.domain.User;
import server.ourhood.global.exception.BaseException;
import server.ourhood.global.exception.BaseResponseStatus;

@Service
@RequiredArgsConstructor
public class CommentService {

	private final CommentRepository commentRepository;
	private final MomentService momentService;

	@Transactional(readOnly = true)
	public Comment findCommentById(Long commentId) {
		return commentRepository.findById(commentId)
			.orElseThrow(() -> new BaseException(BaseResponseStatus.NOT_FOUND_COMMENT));
	}

	@Transactional
	public CommentCreateResponse createComment(CommentCreateRequest request, User user) {
		Moment moment = momentService.findMomentById(request.momentId());
		Comment parent = null;
		if (request.parentId() != null) {
			parent = findCommentById(request.parentId());
			if (parent.isReply()) {
				throw new BaseException(BaseResponseStatus.INVALID_COMMENT_LEVEL);
			}
		}
		Comment comment = CommentConverter.toComment(user, moment, request.commentContent(), parent);
		commentRepository.save(comment);
		return new CommentCreateResponse(comment.getId());
	}

	@Transactional
	public void updateComment(Long commentId, CommentUpdateRequest request, User user) {
		Comment comment = findCommentById(commentId);
		validateCommentOwner(comment, user);
		comment.updateContent(request.commentContent());
	}

	@Transactional
	public void deleteComment(Long commentId, User user) {
		Comment comment = findCommentById(commentId);
		validateCommentOwner(comment, user);
		commentRepository.delete(comment);
	}

	private void validateCommentOwner(Comment comment, User user) {
		if (!comment.getUser().getId().equals(user.getId())) {
			throw new BaseException(BaseResponseStatus.NOT_COMMENT_OWNER);
		}
	}
}
