package server.ourhood.domain.join.api.docs;

import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import server.ourhood.domain.join.dto.request.CreateJoinRequestRequest;
import server.ourhood.domain.join.dto.response.CreateJoinRequestResponse;
import server.ourhood.domain.user.domain.User;
import server.ourhood.global.response.BaseResponse;

@Tag(name = "[4. 참여 요청]", description = "참여 요청 관련 API")
public interface JoinRequestControllerDocs {

	@Operation(summary = "방 참여 요청 생성", description = "특정 방에 참여하고 싶다는 요청을 보냅니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "참여 요청 성공")
	})
	BaseResponse<CreateJoinRequestResponse> createJoinRequest(
		User user,
		@Valid @RequestBody CreateJoinRequestRequest request
	);

	@Operation(summary = "방 참여 요청 수락", description = "사용자의 참여 요청을 수락합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "참여 요청 수락 성공")
	})
	BaseResponse<Void> acceptJoinRequest(
		User user,
		@Parameter(description = "참여 요청 ID", required = true) Long joinRequestId
	);

	@Operation(summary = "방 참여 요청 거절", description = "사용자의 참여 요청을 거절합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "참여 요청 거절 성공")
	})
	BaseResponse<Void> rejectJoinRequest(
		User user,
		@Parameter(description = "참여 요청 ID", required = true) Long joinRequestId
	);

	@Operation(summary = "방 참여 요청 취소", description = "사용자가 보냈던 참여 요청을 스스로 취소합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "참여 요청 취소 성공")
	})
	BaseResponse<Void> cancelJoinRequest(
		User user,
		@Parameter(description = "참여 요청 ID", required = true) Long joinRequestId
	);
}
