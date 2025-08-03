package server.ourhood.domain.auth.application;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import server.ourhood.domain.auth.dto.TokenDto;
import server.ourhood.domain.user.domain.User;

@Component
@RequiredArgsConstructor
public class TokenProvider {

	private final JwtTokenProvider jwtTokenProvider;
	private final UuidTokenProvider uuidTokenProvider;

	public void validateAccessToken(String accessToken) {
		jwtTokenProvider.validateToken(accessToken);
	}

	public TokenDto provideTokens(User user) {
		String accessToken = jwtTokenProvider.generateAccessToken(user.getId().toString());
		String refreshToken = uuidTokenProvider.generateRefreshToken(user.getId().toString());
		return new TokenDto(accessToken, refreshToken);
	}

	public String provideAccessTokenByRefreshToken(String refreshToken) {
		String userId = uuidTokenProvider.getUserId(refreshToken);
		return jwtTokenProvider.generateAccessToken(userId);
	}

	public String getUserIdFromAccessToken(String accessToken) {
		return jwtTokenProvider.getUserId(accessToken);
	}

	public void invalidateRefreshToken(String token) {
		uuidTokenProvider.revokeRefreshToken(token);
	}
}
