package server.ourhood.domain.room.api;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import server.ourhood.domain.room.application.RoomService;
import server.ourhood.domain.room.dto.request.RoomCreateRequest;
import server.ourhood.domain.room.dto.request.RoomUpdateRequest;
import server.ourhood.domain.room.dto.response.GetRoomInvitationResponse;
import server.ourhood.domain.room.dto.response.GetRoomJoinRequestResponse;
import server.ourhood.domain.room.dto.response.GetRoomResponse;
import server.ourhood.domain.room.dto.response.MemberRoomResponse;
import server.ourhood.domain.room.dto.response.RoomCreateResponse;
import server.ourhood.domain.user.domain.User;
import server.ourhood.global.auth.annotation.LoginUser;
import server.ourhood.global.response.BaseResponse;
import server.ourhood.global.util.CloudFrontUtil;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rooms")
public class RoomController {

	private final RoomService roomService;
	private final CloudFrontUtil cloudFrontUtil;

	@PostMapping
	public BaseResponse<RoomCreateResponse> createRoom(@LoginUser User user,
		@Valid @RequestBody RoomCreateRequest request) {
		RoomCreateResponse response = roomService.createRoom(user, request);
		return BaseResponse.success(response);
	}

	@PutMapping("/{roomId}")
	public BaseResponse<Void> updateRoom(@LoginUser User user, @PathVariable Long roomId,
		@Valid @RequestBody RoomUpdateRequest request) {
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
	public BaseResponse<GetRoomResponse> getRoomInfo(@LoginUser User user, @PathVariable Long roomId,
		HttpServletResponse httpResponse) {
		GetRoomResponse response = roomService.getRoomInfo(user, roomId);
		if (response instanceof MemberRoomResponse) {
			List<Cookie> signedCookies = cloudFrontUtil.generateSignedCookies();
			for (Cookie cookie : signedCookies) {
				httpResponse.addCookie(cookie);
			}
		}
		return BaseResponse.success(response);
	}

	@GetMapping("/{roomId}/join-requests")
	public BaseResponse<GetRoomJoinRequestResponse> getRoomJoinRequests(@LoginUser User user,
		@PathVariable Long roomId) {
		GetRoomJoinRequestResponse response = roomService.getRoomJoinRequests(user, roomId);
		return BaseResponse.success(response);
	}

	@GetMapping("/{roomId}/invitations")
	public BaseResponse<GetRoomInvitationResponse> getRoomInvitations(@LoginUser User user, @PathVariable Long roomId) {
		GetRoomInvitationResponse response = roomService.getRoomInvitations(user, roomId);
		return BaseResponse.success(response);
	}
}
