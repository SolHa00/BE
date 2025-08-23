package server.ourhood.domain.comment.api;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import server.ourhood.domain.comment.api.docs.CommentControllerDocs;
import server.ourhood.domain.comment.application.CommentService;
import server.ourhood.domain.comment.dto.request.CreateCommentRequest;
import server.ourhood.domain.comment.dto.request.UpdateCommentRequest;
import server.ourhood.domain.comment.dto.response.CreateCommentResponse;
import server.ourhood.domain.user.domain.User;
import server.ourhood.global.auth.annotation.LoginUser;
import server.ourhood.global.response.BaseResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController implements CommentControllerDocs {

	private final CommentService commentService;

	@PostMapping
	public BaseResponse<CreateCommentResponse> createComment(@LoginUser User user,
		@Valid @RequestBody CreateCommentRequest request) {
		return BaseResponse.success(commentService.createComment(user, request));
	}

	@PutMapping("/{commentId}")
	public BaseResponse<Void> updateComment(@LoginUser User user, @PathVariable Long commentId,
		@Valid @RequestBody UpdateCommentRequest request) {
		commentService.updateComment(user, commentId, request);
		return BaseResponse.success();
	}

	@DeleteMapping("/{commentId}")
	public BaseResponse<Void> deleteComment(@LoginUser User user, @PathVariable Long commentId) {
		commentService.deleteComment(user, commentId);
		return BaseResponse.success();
	}
}
