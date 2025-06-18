package server.ourhood.domain.auth.dto.response;

import server.ourhood.domain.user.domain.User;

public record UserResponse(
        Long userId,
        String email
) {
    public UserResponse(User user) {
        this(user.getId(), user.getEmail());
    }
}
