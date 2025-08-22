package server.ourhood.domain.auth.api.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import server.ourhood.domain.auth.domain.OAuthType;
import server.ourhood.domain.auth.dto.response.AuthResponse;
import server.ourhood.domain.auth.dto.response.OAuthUrlResponse;
import server.ourhood.domain.auth.dto.response.TokenResponse;
import server.ourhood.global.response.BaseResponse;

@Tag(name = "[1. 인증]", description = "인증 관련 API")
public interface AuthControllerDocs {

	@SecurityRequirements
	@Operation(
		summary = "소셜 로그인 인가 URL 요청",
		description = "지정된 OAuthType에 따른 소셜 로그인 인가 코드 요청 URL을 반환합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "URL 반환 성공")
	})
	BaseResponse<OAuthUrlResponse> redirectAuthRequestUrl(
		@Parameter(description = "Oauth 제공자", required = true, example = "kakao")
		OAuthType oauthType
	);

	@SecurityRequirements
	@Operation(
		summary = "소셜 로그인 및 토큰 발급",
		description = "소셜 로그인 인가 코드를 통해 토큰(Access Token & Refresh Token)을 발급받습니다.")
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "로그인 성공. 응답 헤더의 Set-Cookie를 통해 Refresh Token이 발급됩니다.",
			headers = @Header(name = "Set-Cookie", description = "Refresh Token을 담고 있는 쿠키", schema = @Schema(type = "string"))
		)
	})
	BaseResponse<AuthResponse> login(
		@Parameter(description = "소셜 공급자 타입 (예: kakao, google)", required = true, example = "kakao")
		OAuthType oauthType,
		@Parameter(description = "소셜 서버로부터 전달받은 인가 코드", required = true)
		String code,
		@Parameter(hidden = true) HttpServletResponse servletResponse
	);

	@SecurityRequirements
	@Operation(
		summary = "토큰 재발급",
		description = "토큰(Access Token)을 재발급받습니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "토큰 재발급 성공"),
		@ApiResponse(responseCode = "401", description = "Refresh Token 만료")
	})
	BaseResponse<TokenResponse> refresh(
		@Parameter(hidden = true) HttpServletRequest servletRequest
	);

	@Operation(
		summary = "로그아웃",
		description = "사용자 로그아웃을 처리하고 Refresh Token을 무효화합니다.")
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "로그아웃 성공",
			headers = @Header(name = "Set-Cookie", description = "만료된 Refresh Token 쿠키", schema = @Schema(type = "string"))
		),
		@ApiResponse(responseCode = "401", description = "유효하지 않은 Refresh Token")
	})
	BaseResponse<Void> logout(
		@Parameter(hidden = true) HttpServletRequest servletRequest,
		@Parameter(hidden = true) HttpServletResponse servletResponse
	);
}
