package server.ourhood.domain.comment.api.docs;

import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import server.ourhood.domain.comment.dto.request.CreateCommentRequest;
import server.ourhood.domain.comment.dto.request.UpdateCommentRequest;
import server.ourhood.domain.comment.dto.response.CreateCommentResponse;
import server.ourhood.domain.user.domain.User;
import server.ourhood.global.response.BaseResponse;

@Tag(name = "[8. 댓글]", description = "댓글 관련 API")
public interface CommentControllerDocs {

	@Operation(summary = "댓글 또는 대댓글 작성", description = "모먼트 또는 다른 댓글에 댓글을 작성합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "댓글 작성 성공")
	})
	BaseResponse<CreateCommentResponse> createComment(
		User user,
		@Valid @RequestBody CreateCommentRequest request
	);

	@Operation(summary = "댓글 수정", description = "자신이 작성한 댓글의 내용을 수정합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "댓글 수정 성공")
	})
	BaseResponse<Void> updateComment(
		User user,
		@Parameter(description = "댓글 ID", required = true) Long commentId,
		@Valid @RequestBody UpdateCommentRequest request
	);

	@Operation(summary = "댓글 삭제", description = "자신이 작성한 댓글을 삭제합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "댓글 삭제 성공")
	})
	BaseResponse<Void> deleteComment(
		User user,
		@Parameter(description = "댓글 ID", required = true) Long commentId
	);
}
