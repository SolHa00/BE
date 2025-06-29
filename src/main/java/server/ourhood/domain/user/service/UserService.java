package server.ourhood.domain.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import server.ourhood.domain.user.domain.User;
import server.ourhood.domain.user.repository.UserRepository;
import server.ourhood.global.exception.BaseException;
import server.ourhood.global.exception.BaseResponseStatus;
import server.ourhood.global.s3.S3Service;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final S3Service s3Service;

	@Transactional(readOnly = true)
	public User findUserById(Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new BaseException(BaseResponseStatus.NOT_FOUND_USER));
	}

	@Transactional
	public void createProfileImage(Long userId, MultipartFile profileImage) {
		User user = findUserById(userId);
		String profileImageUrl = s3Service.upload(profileImage);
		user.updateProfileImage(profileImageUrl);
	}

	@Transactional
	public void updateProfileImage(Long userId, MultipartFile profileImage) {
		User user = findUserById(userId);
		if (user.getProfileImageUrl() != null) {
			deleteProfileImage(user.getProfileImageUrl());
		}
		String profileImageUrl = s3Service.upload(profileImage);
		user.updateProfileImage(profileImageUrl);
	}

	@Transactional
	public void deleteProfileImage(Long userId) {
		User user = findUserById(userId);
		if (user.getProfileImageUrl() != null) {
			deleteProfileImage(user.getProfileImageUrl());
			user.updateProfileImage(null);
		}
	}

	private void deleteProfileImage(String profileImageUrl) {
		String fileName = extractFileNameFromUrl(profileImageUrl);
		s3Service.deleteFile(fileName);
	}

	private String extractFileNameFromUrl(String url) {
		return url.substring(url.lastIndexOf("/") + 1);
	}
}
