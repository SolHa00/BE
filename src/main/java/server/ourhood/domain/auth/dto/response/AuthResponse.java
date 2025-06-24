package server.ourhood.domain.auth.dto.response;

public record AuthResponse(
	TokenResponse token,
	UserResponse user) {
}
