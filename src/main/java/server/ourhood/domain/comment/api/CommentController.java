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
import server.ourhood.domain.comment.dto.request.CommentCreateRequest;
import server.ourhood.domain.comment.dto.request.CommentUpdateRequest;
import server.ourhood.domain.comment.dto.response.CommentCreateResponse;
import server.ourhood.domain.user.domain.User;
import server.ourhood.global.auth.annotation.LoginUser;
import server.ourhood.global.response.BaseResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController implements CommentControllerDocs {

	private final CommentService commentService;

	@PostMapping
	public BaseResponse<CommentCreateResponse> createComment(@LoginUser User user,
		@Valid @RequestBody CommentCreateRequest request) {
		CommentCreateResponse response = commentService.createComment(user, request);
		return BaseResponse.success(response);
	}

	@PutMapping("/{commentId}")
	public BaseResponse<Void> updateComment(@LoginUser User user, @PathVariable Long commentId,
		@Valid @RequestBody CommentUpdateRequest request) {
		commentService.updateComment(user, commentId, request);
		return BaseResponse.success();
	}

	@DeleteMapping("/{commentId}")
	public BaseResponse<Void> deleteComment(@LoginUser User user, @PathVariable Long commentId) {
		commentService.deleteComment(user, commentId);
		return BaseResponse.success();
	}
}
