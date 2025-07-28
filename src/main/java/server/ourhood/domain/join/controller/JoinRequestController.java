package server.ourhood.domain.join.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import server.ourhood.domain.join.dto.request.JoinRequestCreateRequest;
import server.ourhood.domain.join.dto.response.JoinRequestCreateResponse;
import server.ourhood.domain.join.service.JoinRequestService;
import server.ourhood.domain.user.domain.User;
import server.ourhood.global.auth.annotation.LoginUser;
import server.ourhood.global.response.BaseResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rooms/join-requests")
public class JoinRequestController {

	private final JoinRequestService joinRequestService;

	@PostMapping
	public BaseResponse<JoinRequestCreateResponse> createJoinRequest(@LoginUser User user,
		@Valid @RequestBody JoinRequestCreateRequest request) {
		JoinRequestCreateResponse response = joinRequestService.createJoinRequest(user, request);
		return BaseResponse.success(response);
	}

	@PostMapping("/{joinRequestId}/accept")
	public BaseResponse<Void> acceptJoinRequest(@LoginUser User user, @PathVariable Long joinRequestId) {
		joinRequestService.accept(user, joinRequestId);
		return BaseResponse.success();
	}

	@PostMapping("/{joinRequestId}/reject")
	public BaseResponse<Void> rejectJoinRequest(@LoginUser User user, @PathVariable Long joinRequestId) {
		joinRequestService.reject(user, joinRequestId);
		return BaseResponse.success();
	}

	@PostMapping("/{joinRequestId}/cancel")
	public BaseResponse<Void> cancelJoinRequest(@LoginUser User user, @PathVariable Long joinRequestId) {
		joinRequestService.cancel(user, joinRequestId);
		return BaseResponse.success();
	}
}
