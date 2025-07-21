package server.ourhood.domain.image.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import server.ourhood.domain.image.dto.request.MomentImageCreateRequest;
import server.ourhood.domain.image.dto.request.RoomThumbnailImageCreateRequest;
import server.ourhood.domain.image.dto.response.PresignedUrlResponse;
import server.ourhood.domain.image.service.ImageService;
import server.ourhood.domain.user.domain.User;
import server.ourhood.global.auth.annotation.LoginUser;
import server.ourhood.global.response.BaseResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/images")
public class ImageController {

	private final ImageService imageService;

	@PostMapping("/rooms/upload-url")
	public BaseResponse<PresignedUrlResponse> createRoomThumbnailPresignedUrl(@LoginUser User user,
		@Valid @RequestBody RoomThumbnailImageCreateRequest request) {
		PresignedUrlResponse response = imageService.createRoomThumbnailPresignedUrl(user, request);
		return BaseResponse.success(response);
	}

	@PostMapping("/moments/upload-url")
	public BaseResponse<PresignedUrlResponse> createMomentPresignedUrl(@LoginUser User user,
		@Valid @RequestBody MomentImageCreateRequest request) {
		PresignedUrlResponse response = imageService.createMomentPresignedUrl(user, request);
		return BaseResponse.success(response);
	}
}
