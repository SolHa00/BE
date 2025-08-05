package server.ourhood.domain.auth.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import server.ourhood.domain.auth.application.AuthService;
import server.ourhood.domain.auth.domain.OAuthType;
import server.ourhood.domain.auth.dto.response.AuthResponse;
import server.ourhood.domain.auth.dto.response.AuthServiceResponse;
import server.ourhood.domain.auth.dto.response.OAuthUrlResponse;
import server.ourhood.domain.auth.dto.response.TokenResponse;
import server.ourhood.global.auth.annotation.PublicApi;
import server.ourhood.global.auth.annotation.SecuredApi;
import server.ourhood.global.response.BaseResponse;
import server.ourhood.global.util.CookieUtil;

@PublicApi
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

	private final AuthService authService;
	private final CookieUtil cookieUtil;

	@GetMapping("/{oauthType}")
	public BaseResponse<OAuthUrlResponse> redirectAuthRequestUrl(@PathVariable OAuthType oauthType) {
		OAuthUrlResponse response = authService.getAuthCodeRequestUrl(oauthType);
		return BaseResponse.success(response);
	}

	@PostMapping("/login/{oauthType}")
	public BaseResponse<AuthResponse> login(
		@PathVariable OAuthType oauthType,
		@RequestParam String code,
		HttpServletResponse servletResponse) {
		AuthServiceResponse serviceResponse = authService.loginAndGenerateToken(oauthType, code);
		AuthResponse response = serviceResponse.clientResponse();
		cookieUtil.addCookie(servletResponse, serviceResponse.refreshToken());
		return BaseResponse.success(response);
	}

	@PostMapping("/refresh")
	public BaseResponse<TokenResponse> refresh(HttpServletRequest servletRequest) {
		String refreshToken = cookieUtil.getRefreshToken(servletRequest.getCookies());
		TokenResponse response = authService.refresh(refreshToken);
		return BaseResponse.success(response);
	}

	@SecuredApi
	@PostMapping("/logout")
	public BaseResponse<Void> logout(
		HttpServletRequest servletRequest,
		HttpServletResponse servletResponse) {
		String refreshToken = cookieUtil.getRefreshToken(servletRequest.getCookies());
		authService.logout(refreshToken);
		cookieUtil.deleteCookie(servletResponse);
		return BaseResponse.success();
	}
}
