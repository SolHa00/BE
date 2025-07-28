package server.ourhood.domain.moment.controller;

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
import server.ourhood.domain.moment.dto.request.MomentCreateRequest;
import server.ourhood.domain.moment.dto.request.MomentUpdateRequest;
import server.ourhood.domain.moment.dto.response.GetMomentResponse;
import server.ourhood.domain.moment.dto.response.MomentCreateResponse;
import server.ourhood.domain.moment.service.MomentService;
import server.ourhood.domain.user.domain.User;
import server.ourhood.global.auth.annotation.LoginUser;
import server.ourhood.global.response.BaseResponse;
import server.ourhood.global.util.CloudFrontUtil;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/moments")
public class MomentController {

	private final MomentService momentService;
	private final CloudFrontUtil cloudFrontUtil;

	@PostMapping
	public BaseResponse<MomentCreateResponse> createMoment(@LoginUser User user,
		@Valid @RequestBody MomentCreateRequest request) {
		MomentCreateResponse response = momentService.createMoment(user, request);
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

	@GetMapping("/{momentId}")
	public BaseResponse<GetMomentResponse> getMoment(@LoginUser User user, @PathVariable Long momentId,
		HttpServletResponse httpResponse
	) {
		GetMomentResponse response = momentService.getMoment(user, momentId);
		List<Cookie> signedCookies = cloudFrontUtil.generateSignedCookies();
		for (Cookie cookie : signedCookies) {
			httpResponse.addCookie(cookie);
		}
		return BaseResponse.success(response);
	}
}
