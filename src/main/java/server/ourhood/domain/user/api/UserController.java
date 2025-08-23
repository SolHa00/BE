package server.ourhood.domain.user.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import server.ourhood.domain.user.api.docs.UserControllerDocs;
import server.ourhood.domain.user.application.UserService;
import server.ourhood.domain.user.domain.User;
import server.ourhood.domain.user.dto.request.UpdateUserInfoRequest;
import server.ourhood.domain.user.dto.response.GetUserInfoResponse;
import server.ourhood.global.auth.annotation.LoginUser;
import server.ourhood.global.response.BaseResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController implements UserControllerDocs {

	private final UserService userService;

	@GetMapping("/me")
	public BaseResponse<GetUserInfoResponse> getUserInfo(@LoginUser User user) {
		return BaseResponse.success(userService.getUserInfo(user));
	}

	@PutMapping("/me")
	public BaseResponse<Void> updateUserInfo(@LoginUser User user,
		@Valid @RequestBody UpdateUserInfoRequest request) {
		userService.updateUserInfo(user.getId(), request);
		return BaseResponse.success();
	}
}
