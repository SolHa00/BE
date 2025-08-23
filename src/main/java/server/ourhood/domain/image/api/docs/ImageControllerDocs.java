package server.ourhood.domain.image.api.docs;

import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import server.ourhood.domain.image.dto.request.CreateMomentImageRequest;
import server.ourhood.domain.image.dto.request.CreateRoomThumbnailImageRequest;
import server.ourhood.domain.image.dto.response.GetPresignedUrlResponse;
import server.ourhood.domain.user.domain.User;
import server.ourhood.global.response.BaseResponse;

@Tag(name = "[6. 이미지]", description = "이미지 관련 API")
public interface ImageControllerDocs {

	@Operation(
		summary = "방 썸네일 이미지 업로드용 Presigned URL 생성",
		description = "S3에 방 썸네일 이미지를 업로드하기 위한 Presigned URL을 생성합니다. 클라이언트는 이 API로 URL을 받은 후, 해당 URL에 PUT 요청으로 이미지 파일을 전송해야 합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Presigned URL 생성 성공")
	})
	BaseResponse<GetPresignedUrlResponse> createRoomThumbnailPresignedUrl(
		User user,
		@Valid @RequestBody CreateRoomThumbnailImageRequest request
	);

	@Operation(
		summary = "모먼트 이미지 업로드용 Presigned URL 생성",
		description = "S3에 모먼트 이미지를 업로드하기 위한 Presigned URL을 생성합니다. 클라이언트는 이 API로 URL을 받은 후, 해당 URL에 PUT 요청으로 이미지 파일을 전송해야 합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Presigned URL 생성 성공")
	})
	BaseResponse<GetPresignedUrlResponse> createMomentPresignedUrl(
		User user,
		@Valid @RequestBody CreateMomentImageRequest request
	);
}
