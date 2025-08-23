package server.ourhood.domain.room.api;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import server.ourhood.domain.room.api.docs.RoomControllerDocs;
import server.ourhood.domain.room.application.RoomService;
import server.ourhood.domain.room.dto.request.CreateRoomRequest;
import server.ourhood.domain.room.dto.request.RoomSearchCondition;
import server.ourhood.domain.room.dto.request.UpdateRoomRequest;
import server.ourhood.domain.room.dto.response.CreateRoomResponse;
import server.ourhood.domain.room.dto.response.GetRoomDetailResponse;
import server.ourhood.domain.room.dto.response.GetRoomInvitationResponse;
import server.ourhood.domain.room.dto.response.GetRoomJoinRequestResponse;
import server.ourhood.domain.room.dto.response.GetRoomListResponse;
import server.ourhood.domain.room.dto.response.GetRoomMembersResponse;
import server.ourhood.domain.room.dto.response.GetRoomMomentsResponse;
import server.ourhood.domain.room.dto.response.MemberRoomDetailResponse;
import server.ourhood.domain.user.domain.User;
import server.ourhood.global.auth.annotation.LoginUser;
import server.ourhood.global.auth.annotation.PublicApi;
import server.ourhood.global.response.BaseResponse;
import server.ourhood.global.util.CloudFrontUtil;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rooms")
public class RoomController implements RoomControllerDocs {

	private final RoomService roomService;
	private final CloudFrontUtil cloudFrontUtil;

	@PostMapping
	public BaseResponse<CreateRoomResponse> createRoom(@LoginUser User user,
		@Valid @RequestBody CreateRoomRequest request) {
		return BaseResponse.success(roomService.createRoom(user, request));
	}

	@PutMapping("/{roomId}")
	public BaseResponse<Void> updateRoom(@LoginUser User user, @PathVariable Long roomId,
		@Valid @RequestBody UpdateRoomRequest request) {
		roomService.updateRoom(user, roomId, request);
		return BaseResponse.success();
	}

	@DeleteMapping("/{roomId}")
	public BaseResponse<Void> deleteRoom(@LoginUser User user, @PathVariable Long roomId) {
		roomService.deleteRoom(user, roomId);
		return BaseResponse.success();
	}

	@DeleteMapping("/{roomId}/leave")
	public BaseResponse<Void> leaveRoom(@LoginUser User user, @PathVariable Long roomId) {
		roomService.leave(user, roomId);
		return BaseResponse.success();
	}

	@GetMapping("/{roomId}")
	public BaseResponse<GetRoomDetailResponse> getRoomDetail(@LoginUser User user, @PathVariable Long roomId,
		HttpServletResponse httpResponse) {
		GetRoomDetailResponse response = roomService.getRoomDetail(user, roomId);
		if (response instanceof MemberRoomDetailResponse) {
			List<Cookie> signedCookies = cloudFrontUtil.generateSignedCookies();
			for (Cookie cookie : signedCookies) {
				httpResponse.addCookie(cookie);
			}
		}
		return BaseResponse.success(response);
	}

	@GetMapping("/{roomId}/members")
	public BaseResponse<GetRoomMembersResponse> getRoomMembersInfo(@LoginUser User user, @PathVariable Long roomId) {
		return BaseResponse.success(roomService.getRoomMembersInfo(user, roomId));
	}

	@GetMapping("/{roomId}/moments")
	public BaseResponse<GetRoomMomentsResponse> getRoomMomentsInfo(@LoginUser User user, @PathVariable Long roomId) {
		return BaseResponse.success(roomService.getRoomMomentsInfo(user, roomId));
	}

	@GetMapping("/{roomId}/join-requests")
	public BaseResponse<GetRoomJoinRequestResponse> getRoomJoinRequests(@LoginUser User user,
		@PathVariable Long roomId) {
		return BaseResponse.success(roomService.getRoomJoinRequests(user, roomId));
	}

	@GetMapping("/{roomId}/invitations")
	public BaseResponse<GetRoomInvitationResponse> getRoomInvitations(@LoginUser User user, @PathVariable Long roomId) {
		return BaseResponse.success(roomService.getRoomInvitations(user, roomId));
	}

	@PublicApi
	@GetMapping
	public BaseResponse<GetRoomListResponse> getRooms(
		@RequestParam(required = false) RoomSearchCondition condition,
		@RequestParam(required = false) String q,
		@SortDefault(sort = "createdAt", direction = Sort.Direction.DESC) Sort sort) {
		return BaseResponse.success(roomService.getRooms(condition, q, sort));
	}
}
