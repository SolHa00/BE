package server.ourhood.domain.user.api.docs;

import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import server.ourhood.domain.user.domain.User;
import server.ourhood.domain.user.dto.request.UpdateUserInfoRequest;
import server.ourhood.domain.user.dto.response.GetUserInfoResponse;
import server.ourhood.global.response.BaseResponse;

@Tag(name = "[2. 회원]", description = "회원 관련 API")
public interface UserControllerDocs {

	@Operation(summary = "내 정보 조회", description = "로그인된 사용자의 정보를 조회합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "내 정보 조회 성공")
	})
	BaseResponse<GetUserInfoResponse> getUserInfo(
		User user
	);

	@Operation(summary = "내 정보 수정", description = "로그인된 사용자의 닉네임을 수정합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "내 정보 수정 성공")
	})
	BaseResponse<Void> updateUserInfo(
		User user,
		@Valid @RequestBody UpdateUserInfoRequest request
	);
}
