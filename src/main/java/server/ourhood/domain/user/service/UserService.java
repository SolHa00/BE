package server.ourhood.domain.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import server.ourhood.domain.user.domain.User;
import server.ourhood.domain.user.repository.UserRepository;
import server.ourhood.global.exception.BaseException;
import server.ourhood.global.exception.BaseResponseStatus;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	@Transactional(readOnly = true)
	public User findUserByNickname(String nickname) {
		return userRepository.findByNickname(nickname)
			.orElseThrow(() -> new BaseException(BaseResponseStatus.NOT_FOUND_USER));
	}
}
