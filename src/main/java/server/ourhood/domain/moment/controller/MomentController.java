package server.ourhood.domain.moment.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import server.ourhood.domain.moment.dto.request.MomentCreateRequest;
import server.ourhood.domain.moment.dto.request.MomentUpdateRequest;
import server.ourhood.domain.moment.dto.response.MomentCreateResponse;
import server.ourhood.domain.moment.service.MomentService;
import server.ourhood.domain.user.domain.User;
import server.ourhood.global.auth.annotation.LoginUser;
import server.ourhood.global.response.BaseResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/moments")
public class MomentController {

	private final MomentService momentService;

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public BaseResponse<MomentCreateResponse> createMoment(@LoginUser User user,
		@ModelAttribute MomentCreateRequest request,
		@RequestPart MultipartFile momentImage) {
		MomentCreateResponse response = momentService.createMoment(user, request, momentImage);
		return BaseResponse.success(response);
	}

	@PutMapping("/{momentId}")
	public BaseResponse<Void> updateMoment(@LoginUser User user, @PathVariable Long momentId,
		@RequestBody MomentUpdateRequest request) {
		momentService.updateMoment(user, momentId, request);
		return BaseResponse.success();
	}

	@DeleteMapping("/{momentId}")
	public BaseResponse<Void> deleteMoment(@LoginUser User user, @PathVariable Long momentId) {
		momentService.deleteMoment(user, momentId);
		return BaseResponse.success();
	}
}
