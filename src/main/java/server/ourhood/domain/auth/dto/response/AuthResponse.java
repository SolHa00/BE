package server.ourhood.domain.auth.dto.response;

import server.ourhood.domain.auth.dto.TokenDto;
import server.ourhood.domain.user.domain.User;

public record AuthResponse(
        TokenResponse token,
        UserResponse user
) {
    public AuthResponse(TokenDto tokens, User user) {
        this(new TokenResponse(tokens.accessToken(), tokens.refreshToken()),
                new UserResponse(user));
    }
}
