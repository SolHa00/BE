package server.ourhood.domain.auth.dto.response;

import server.ourhood.domain.auth.dto.TokenDto;
import server.ourhood.domain.user.domain.User;

public record AuthServiceResponse(
	AuthResponse clientResponse,
	String refreshToken
) {
	public AuthServiceResponse(TokenDto tokens, User user) {
		this(
			new AuthResponse(
				new TokenResponse(tokens.accessToken()),
				new UserResponse(user)
			),
			tokens.refreshToken()
		);
	}
}
