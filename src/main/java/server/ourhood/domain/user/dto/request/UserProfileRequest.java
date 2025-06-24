package server.ourhood.domain.user.dto.request;

import org.springframework.web.multipart.MultipartFile;

public record UserProfileRequest(MultipartFile profileImage) {
}
