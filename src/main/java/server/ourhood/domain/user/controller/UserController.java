package server.ourhood.domain.user.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import server.ourhood.domain.user.domain.User;
import server.ourhood.domain.user.dto.request.UserProfileRequest;
import server.ourhood.domain.user.service.UserService;
import server.ourhood.global.auth.annotation.LoginUser;
import server.ourhood.global.response.BaseResponse;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@PostMapping("/me/profile-image")
	public BaseResponse<Void> addProfileImage(@LoginUser User user, UserProfileRequest request) {
		userService.createProfileImage(user, request.profileImage());
		return BaseResponse.success();
	}
}
