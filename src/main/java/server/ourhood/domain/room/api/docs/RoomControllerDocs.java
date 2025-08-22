package server.ourhood.domain.room.api.docs;

import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import server.ourhood.domain.room.dto.request.RoomCreateRequest;
import server.ourhood.domain.room.dto.request.RoomSearchCondition;
import server.ourhood.domain.room.dto.request.RoomUpdateRequest;
import server.ourhood.domain.room.dto.response.GetRoomDetailResponse;
import server.ourhood.domain.room.dto.response.GetRoomInvitationResponse;
import server.ourhood.domain.room.dto.response.GetRoomJoinRequestResponse;
import server.ourhood.domain.room.dto.response.GetRoomListResponse;
import server.ourhood.domain.room.dto.response.GetRoomMembersResponse;
import server.ourhood.domain.room.dto.response.GetRoomMomentsResponse;
import server.ourhood.domain.room.dto.response.RoomCreateResponse;
import server.ourhood.domain.user.domain.User;
import server.ourhood.global.response.BaseResponse;

@Tag(name = "[3. 방]", description = "방 관련 API")
public interface RoomControllerDocs {

	@Operation(summary = "방 생성", description = "새로운 방을 생성합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "방 생성 성공")
	})
	BaseResponse<RoomCreateResponse> createRoom(
		User user,
		@Valid @RequestBody RoomCreateRequest request
	);

	@Operation(summary = "방 정보 수정", description = "방의 정보를 수정합니다. 방장만 가능합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "방 정보 수정 성공")
	})
	BaseResponse<Void> updateRoom(
		User user,
		@Parameter(description = "방 ID", required = true) Long roomId,
		@Valid @RequestBody RoomUpdateRequest request
	);

	@Operation(summary = "방 삭제", description = "방을 삭제합니다. 방장만 가능합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "방 삭제 성공")
	})
	BaseResponse<Void> deleteRoom(
		User user,
		@Parameter(description = "방 ID", required = true) Long roomId);

	@Operation(summary = "방 나가기", description = "참여하고 있는 방에서 나갑니다. 방장은 나갈 수 없습니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "방 나가기 성공")
	})
	BaseResponse<Void> leaveRoom(
		User user,
		@Parameter(description = "방 ID", required = true) Long roomId);

	@Operation(summary = "방 상세 정보 조회", description = "방의 상세 정보를 조회합니다. 방 멤버일 경우 CloudFront 서명 쿠키가 발급됩니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "방 상세 정보 조회 성공", headers = {
			@Header(name = "Set-Cookie", description = "CloudFront-Policy, CloudFront-Key-Pair-Id, CloudFront-Signature 서명 쿠키 (방 멤버인 경우)", schema = @Schema(type = "string"))
		})
	})
	BaseResponse<GetRoomDetailResponse> getRoomDetail(
		User user,
		@Parameter(description = "방 ID", required = true) Long roomId,
		@Parameter(hidden = true) HttpServletResponse httpResponse);

	@Operation(summary = "방 멤버 목록 조회", description = "방에 참여중인 멤버들의 목록을 조회합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "방 멤버 목록 조회 성공")
	})
	BaseResponse<GetRoomMembersResponse> getRoomMembersInfo(
		User user,
		@Parameter(description = "방 ID", required = true) Long roomId);

	@Operation(summary = "방 모먼트 목록 조회", description = "방 모먼트 목록을 조회합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "방 모먼트 목록 조회 성공")
	})
	BaseResponse<GetRoomMomentsResponse> getRoomMomentsInfo(
		User user,
		@Parameter(description = "방 ID", required = true) Long roomId);

	@Operation(summary = "방 참여 요청 목록 조회", description = "방 참여 요청 목록을 조회합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "방 참여 요청 목록 조회 성공")
	})
	BaseResponse<GetRoomJoinRequestResponse> getRoomJoinRequests(
		User user,
		@Parameter(description = "방 ID", required = true) Long roomId);

	@Operation(summary = "방 초대 요청 목록 조회", description = "방 초대 요청 목록을 조회합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "방 초대 요청 목록 조회 성공")
	})
	BaseResponse<GetRoomInvitationResponse> getRoomInvitations(
		User user,
		@Parameter(description = "방 ID", required = true) Long roomId);

	@SecurityRequirements
	@Operation(summary = "방 목록 검색 및 조회", description = "전체 방 목록을 조건에 따라 검색하고 정렬하여 조회합니다. 인증이 필요없는 공개 API 입니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "방 목록 조회 성공")
	})
	BaseResponse<GetRoomListResponse> getRooms(
		@Parameter(description = "검색 조건 (예: room, host)", schema = @Schema(type = "string")) RoomSearchCondition condition,
		@Parameter(description = "검색어", schema = @Schema(type = "string")) String q,
		@Parameter(description = "정렬 조건 (형식: `property,direction`). direction은 `asc` 또는 `desc`를 사용할 수 있습니다. 기본 정렬은 `createdAt,desc` 입니다.",
			schema = @Schema(type = "string", example = "createdAt,asc")) Sort sort
	);
}
