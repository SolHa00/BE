package server.ourhood.domain.room.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import server.ourhood.domain.room.dto.request.RoomCreateRequest;
import server.ourhood.domain.room.dto.request.RoomUpdateRequest;
import server.ourhood.domain.room.dto.response.RoomCreateResponse;
import server.ourhood.domain.room.service.RoomService;
import server.ourhood.domain.user.domain.User;
import server.ourhood.global.auth.annotation.LoginUser;
import server.ourhood.global.response.BaseResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rooms")
public class RoomController {

	private final RoomService roomService;

	@PostMapping
	public BaseResponse<RoomCreateResponse> addRoom(@LoginUser User user, @ModelAttribute RoomCreateRequest request,
		@RequestPart(required = false) MultipartFile thumbnailImage) {
		RoomCreateResponse response = roomService.createRoom(user, request, thumbnailImage);
		return BaseResponse.success(response);
	}

	@PutMapping("/{roomId}")
	public BaseResponse<Void> fixRoom(@LoginUser User user, @PathVariable Long roomId,
		@ModelAttribute RoomUpdateRequest request,
		@RequestPart(required = false) MultipartFile thumbnailImage) {
		roomService.updateRoom(user, roomId, request, thumbnailImage);
		return BaseResponse.success();
	}

	@DeleteMapping("/{roomId}")
	public BaseResponse<Void> removeRoom(@LoginUser User user, @PathVariable Long roomId) {
		roomService.deleteRoom(user, roomId);
		return BaseResponse.success();
	}

	@DeleteMapping("/{roomId}/leave")
	public BaseResponse<Void> leaveRoom(@LoginUser User user, @PathVariable Long roomId) {
		roomService.leaveRoom(user, roomId);
		return BaseResponse.success();
	}
}
