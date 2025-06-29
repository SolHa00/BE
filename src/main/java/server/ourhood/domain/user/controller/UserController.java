package server.ourhood.domain.user.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import server.ourhood.domain.user.domain.User;
import server.ourhood.domain.user.dto.request.UserProfileRequest;
import server.ourhood.domain.user.service.UserService;
import server.ourhood.global.auth.annotation.LoginUser;
import server.ourhood.global.response.BaseResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

	private final UserService userService;

	@PostMapping("/me/profile-image")
	public BaseResponse<Void> addProfileImage(@LoginUser User user, UserProfileRequest request) {
		userService.createProfileImage(user.getId(), request.profileImage());
		return BaseResponse.success();
	}

	@PutMapping("/me/profile-image")
	public BaseResponse<Void> fixProfileImage(@LoginUser User user, UserProfileRequest request) {
		userService.updateProfileImage(user.getId(), request.profileImage());
		return BaseResponse.success();
	}

	@DeleteMapping("/me/profile-image")
	public BaseResponse<Void> removeProfileImage(@LoginUser User user) {
		userService.deleteProfileImage(user.getId());
		return BaseResponse.success();
	}
}
