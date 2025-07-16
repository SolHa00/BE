package server.ourhood.domain.join.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import server.ourhood.domain.join.dto.request.JoinRequestCreateRequest;
import server.ourhood.domain.join.dto.response.JoinRequestCreateResponse;
import server.ourhood.domain.join.service.JoinRequestService;
import server.ourhood.domain.user.domain.User;
import server.ourhood.global.auth.annotation.LoginUser;
import server.ourhood.global.response.BaseResponse;

@RestController
@RequiredArgsConstructor
public class JoinRequestController {

	private final JoinRequestService joinRequestService;

	@PostMapping("/api/join-requests")
	public BaseResponse<JoinRequestCreateResponse> createJoinRequest(@LoginUser User user,
		@RequestBody JoinRequestCreateRequest request) {
		JoinRequestCreateResponse response = joinRequestService.createJoinRequest(user, request);
		return BaseResponse.success(response);
	}

	@PostMapping("/api/join-requests/{joinRequestId}/accept")
	public BaseResponse<Void> acceptJoinRequest(@LoginUser User user, @PathVariable Long joinRequestId) {
		joinRequestService.accept(user, joinRequestId);
		return BaseResponse.success();
	}

	@PostMapping("/api/join-requests/{joinRequestId}/reject")
	public BaseResponse<Void> rejectJoinRequest(@LoginUser User user, @PathVariable Long joinRequestId) {
		joinRequestService.reject(user, joinRequestId);
		return BaseResponse.success();
	}

	@PostMapping("/api/join-requests/{joinRequestId}/cancel")
	public BaseResponse<Void> cancelJoinRequest(@LoginUser User user, @PathVariable Long joinRequestId) {
		joinRequestService.cancel(user, joinRequestId);
		return BaseResponse.success();
	}
}
