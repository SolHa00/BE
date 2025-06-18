package server.ourhood.domain.auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import server.ourhood.domain.auth.domain.OAuthType;
import server.ourhood.domain.auth.dto.request.RefreshTokenRequest;
import server.ourhood.domain.auth.dto.response.AuthResponse;
import server.ourhood.domain.auth.dto.response.OAuthUrlResponse;
import server.ourhood.domain.auth.dto.response.TokenResponse;
import server.ourhood.domain.auth.service.AuthService;
import server.ourhood.global.auth.annotation.PublicApi;
import server.ourhood.global.auth.annotation.SecuredApi;
import server.ourhood.global.response.BaseResponse;

@PublicApi
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	@GetMapping("/{oauthType}")
	public BaseResponse<OAuthUrlResponse> redirectAuthRequestUrl(@PathVariable String oauthType) {
		OAuthType oauthProvider = OAuthType.fromName(oauthType);
		OAuthUrlResponse response = authService.getAuthCodeRequestUrl(oauthProvider);
		return BaseResponse.success(response);
	}

	@PostMapping("/login/{oauthType}")
	public BaseResponse<AuthResponse> login(
		@PathVariable String oauthType,
		@RequestParam String code) {
		OAuthType oauthProvider = OAuthType.fromName(oauthType);
		AuthResponse response = authService.loginAndGenerateToken(oauthProvider, code);
		return BaseResponse.success(response);
	}

	@PostMapping("/refresh")
	public BaseResponse<TokenResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
		TokenResponse response = authService.refreshToken(request);
		return BaseResponse.success(response);
	}

	@SecuredApi
	@PostMapping("/logout")
	public BaseResponse<Void> logout(@RequestBody RefreshTokenRequest request) {
		authService.logout(request.refreshToken());
		return BaseResponse.success();
	}
}
