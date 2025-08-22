package server.ourhood.domain.invitation.api.docs;

import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import server.ourhood.domain.invitation.dto.request.InvitationCreateRequest;
import server.ourhood.domain.invitation.dto.response.InvitationCreateResponse;
import server.ourhood.domain.user.domain.User;
import server.ourhood.global.response.BaseResponse;

@Tag(name = "[5. 초대]", description = "초대 관련 API")
public interface InvitationControllerDocs {

	@Operation(summary = "방으로 사용자 초대", description = "특정 사용자를 방으로 초대합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "초대 성공")
	})
	BaseResponse<InvitationCreateResponse> createInvitation(
		User user,
		@Valid @RequestBody InvitationCreateRequest request
	);

	@Operation(summary = "초대 수락", description = "사용자가 자신에게 온 초대를 수락합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "초대 수락 성공")
	})
	BaseResponse<Void> acceptInvitation(
		User user,
		@Parameter(description = "초대 ID", required = true) Long invitationId
	);

	@Operation(summary = "초대 거절", description = "사용자가 자신에게 온 초대를 거절합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "초대 거절 성공")
	})
	BaseResponse<Void> rejectInvitation(
		User user,
		@Parameter(description = "초대 ID", required = true) Long invitationId
	);

	@Operation(summary = "초대 취소", description = "방에서 보냈던 초대를 취소합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "초대 취소 성공")
	})
	BaseResponse<Void> cancelInvitation(
		User user,
		@Parameter(description = "초대 ID", required = true) Long invitationId
	);
}
