package server.ourhood.domain.comment.service;

import static server.ourhood.global.exception.BaseResponseStatus.*;

import java.util.Optional;

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

	@Transactional(readOnly = true)
	public Comment getByCommentId(Long commentId) {
		return commentRepository.findById(commentId)
			.orElseThrow(() -> new BaseException(NOT_FOUND_COMMENT));
	}

	@Transactional
	public CommentCreateResponse createComment(User user, CommentCreateRequest request) {
		Moment moment = momentService.getByMomentId(request.momentId());
		Comment parent = Optional.ofNullable(request.parentId())
			.map(this::getByCommentId)
			.map(p -> {
				if (p.isReply()) {
					throw new BaseException(INVALID_COMMENT_LEVEL);
				}
				return p;
			})
			.orElse(null);
		Comment comment = Comment.createComment(request.commentContent(), moment, parent, user);
		commentRepository.save(comment);
		return new CommentCreateResponse(comment.getId());
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
