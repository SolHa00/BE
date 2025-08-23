package server.ourhood.domain.moment.api.docs;

import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import server.ourhood.domain.moment.dto.request.CreateMomentRequest;
import server.ourhood.domain.moment.dto.request.UpdateMomentRequest;
import server.ourhood.domain.moment.dto.response.CreateMomentResponse;
import server.ourhood.domain.moment.dto.response.GetMomentCommentResponse;
import server.ourhood.domain.moment.dto.response.GetMomentResponse;
import server.ourhood.domain.user.domain.User;
import server.ourhood.global.response.BaseResponse;

@Tag(name = "[7. 모먼트]", description = "모먼트 관련 API")
public interface MomentControllerDocs {

	@Operation(summary = "모먼트 생성", description = "방에 새로운 모먼트를 생성합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "모먼트 생성 성공")
	})
	BaseResponse<CreateMomentResponse> createMoment(
		User user,
		@Valid @RequestBody CreateMomentRequest request
	);

	@Operation(summary = "모먼트 수정", description = "모먼트의 내용을 수정합니다. 모먼트 소유자만 가능합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "모먼트 수정 성공")
	})
	BaseResponse<Void> updateMoment(
		User user,
		@Parameter(description = "모먼트 ID", required = true) Long momentId,
		@Valid @RequestBody UpdateMomentRequest request
	);

	@Operation(summary = "모먼트 삭제", description = "모먼트를 삭제합니다. 모먼트 소유자만 가능합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "모먼트 삭제 성공")
	})
	BaseResponse<Void> deleteMoment(
		User user,
		@Parameter(description = "모먼트 ID", required = true) Long momentId
	);

	@Operation(summary = "모먼트 상세 조회", description = "모먼트의 상세 정보를 조회합니다. CloudFront 서명 쿠키가 발급됩니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "모먼트 상세 조회 성공", headers = {
			@Header(name = "Set-Cookie", description = "CloudFront-Policy, CloudFront-Key-Pair-Id, CloudFront-Signature 서명 쿠키", schema = @Schema(type = "string"))
		})
	})
	BaseResponse<GetMomentResponse> getMoment(
		User user,
		@Parameter(description = "모먼트 ID", required = true) Long momentId,
		@Parameter(hidden = true) HttpServletResponse httpResponse
	);

	@Operation(summary = "모먼트 댓글 목록 조회", description = "모먼트에 달린 댓글 목록을 조회합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "모먼트 댓글 목록 조회 성공")
	})
	BaseResponse<GetMomentCommentResponse> getMomentComments(
		User user,
		@Parameter(description = "모먼트 ID", required = true) Long momentId
	);
}
