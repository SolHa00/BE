package server.ourhood.domain.join.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import server.ourhood.domain.join.dto.request.JoinRequestCreateRequest;
import server.ourhood.domain.join.dto.request.JoinRequestUpdateRequest;
import server.ourhood.domain.join.dto.response.JoinRequestCreateResponse;
import server.ourhood.domain.join.service.JoinRequestService;
import server.ourhood.domain.user.domain.User;
import server.ourhood.global.auth.annotation.LoginUser;
import server.ourhood.global.response.BaseResponse;

@RestController
@RequiredArgsConstructor
public class JoinRequestController {

	private final JoinRequestService joinRequestService;

	@PostMapping("/api/join-request")
	public BaseResponse<JoinRequestCreateResponse> addJoinRequest(@LoginUser User user,
		@RequestBody JoinRequestCreateRequest request) {
		JoinRequestCreateResponse response = joinRequestService.createJoinRequest(user, request);
		return BaseResponse.success(response);
	}

	@PatchMapping("/api/join-request/{joinRequestId}")
	public BaseResponse<Void> processJoinRequest(@LoginUser User user, @PathVariable Long joinRequestId,
		@RequestBody JoinRequestUpdateRequest request) {
		joinRequestService.processJoinRequest(user, joinRequestId, request);
		return BaseResponse.success();
	}

	@DeleteMapping("/api/join-request/{joinRequestId}")
	public BaseResponse<Void> removeJoinRequest(@LoginUser User user, @PathVariable Long joinRequestId) {
		joinRequestService.deleteJoinRequest(user, joinRequestId);
		return BaseResponse.success();
	}
}
