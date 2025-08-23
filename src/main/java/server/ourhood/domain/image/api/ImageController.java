package server.ourhood.domain.image.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import server.ourhood.domain.image.api.docs.ImageControllerDocs;
import server.ourhood.domain.image.application.ImageService;
import server.ourhood.domain.image.dto.request.CreateMomentImageRequest;
import server.ourhood.domain.image.dto.request.CreateRoomThumbnailImageRequest;
import server.ourhood.domain.image.dto.response.GetPresignedUrlResponse;
import server.ourhood.domain.user.domain.User;
import server.ourhood.global.auth.annotation.LoginUser;
import server.ourhood.global.response.BaseResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/images")
public class ImageController implements ImageControllerDocs {

	private final ImageService imageService;

	@PostMapping("/rooms/upload-url")
	public BaseResponse<GetPresignedUrlResponse> createRoomThumbnailPresignedUrl(@LoginUser User user,
		@Valid @RequestBody CreateRoomThumbnailImageRequest request) {
		return BaseResponse.success(imageService.createRoomThumbnailPresignedUrl(user, request));
	}

	@PostMapping("/moments/upload-url")
	public BaseResponse<GetPresignedUrlResponse> createMomentPresignedUrl(@LoginUser User user,
		@Valid @RequestBody CreateMomentImageRequest request) {
		return BaseResponse.success(imageService.createMomentPresignedUrl(user, request));
	}
}
