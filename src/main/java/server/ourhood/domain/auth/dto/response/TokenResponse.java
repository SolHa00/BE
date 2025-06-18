package server.ourhood.domain.auth.dto.response;

public record TokenResponse(
        String accessToken,
        String refreshToken
) {
}
