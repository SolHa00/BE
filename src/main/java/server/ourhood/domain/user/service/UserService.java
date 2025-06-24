package server.ourhood.domain.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import server.ourhood.domain.user.domain.User;
import server.ourhood.domain.user.repository.UserRepository;
import server.ourhood.global.s3.S3Service;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final S3Service s3Service;

	private static final String PROFILE_IMAGE_PATH = "users/profile";

	@Transactional
	public void createProfileImage(User user, MultipartFile profileImage) {
		String profileImageUrl = s3Service.upload(profileImage, PROFILE_IMAGE_PATH);
		user.updateProfileImage(profileImageUrl);
		userRepository.save(user);
	}
}
