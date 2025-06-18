package server.ourhood.domain.auth.dto;

public record TokenDto(
	String accessToken,
	String refreshToken
) {
}
