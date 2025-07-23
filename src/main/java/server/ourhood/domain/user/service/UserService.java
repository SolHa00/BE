package server.ourhood.domain.user.service;

import static server.ourhood.global.exception.BaseResponseStatus.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import server.ourhood.domain.image.domain.Image;
import server.ourhood.domain.image.service.ImageService;
import server.ourhood.domain.invitation.domain.InvitationStatus;
import server.ourhood.domain.invitation.repository.InvitationRepository;
import server.ourhood.domain.join.domain.JoinRequestStatus;
import server.ourhood.domain.join.repository.JoinRequestRepository;
import server.ourhood.domain.room.repository.RoomRepository;
import server.ourhood.domain.user.domain.User;
import server.ourhood.domain.user.dto.request.UserNicknameUpdateRequest;
import server.ourhood.domain.user.dto.response.MyRoomResponse;
import server.ourhood.domain.user.dto.response.ReceivedInvitationResponse;
import server.ourhood.domain.user.dto.response.SentJoinRequestResponse;
import server.ourhood.domain.user.dto.response.UserInfoResponse;
import server.ourhood.domain.user.repository.UserRepository;
import server.ourhood.global.exception.BaseException;
import server.ourhood.global.util.CloudFrontUtil;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final RoomRepository roomRepository;
	private final InvitationRepository invitationRepository;
	private final JoinRequestRepository joinRequestRepository;
	private final CloudFrontUtil cloudFrontUtil;
	private final ImageService imageService;

	@Transactional(readOnly = true)
	public User findUserById(Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new BaseException(NOT_FOUND_USER));
	}

	@Transactional(readOnly = true)
	public User findUserByNickname(String nickname) {
		return userRepository.findByNickname(nickname)
			.orElseThrow(() -> new BaseException(NOT_FOUND_USER));
	}

	@Transactional(readOnly = true)
	public UserInfoResponse getUserInfo(User user) {
		List<MyRoomResponse> myRooms = roomRepository.findAllByMember(user).stream()
			.map(room -> {
				Image thumbnailImage = imageService.findImageByKey(room.getImageKey());
				String thumbnailUrl =
					(thumbnailImage != null) ? cloudFrontUtil.getPublicUrl(thumbnailImage.getFileName()) : null;
				return MyRoomResponse.from(room, thumbnailUrl);
			})
			.collect(Collectors.toList());

		List<ReceivedInvitationResponse> receivedInvitations = invitationRepository.findByInviteeAndStatus(user,
				InvitationStatus.REQUESTED).stream()
			.map(ReceivedInvitationResponse::from)
			.collect(Collectors.toList());

		List<SentJoinRequestResponse> sentJoinRequests = joinRequestRepository.findByRequesterAndStatus(user,
				JoinRequestStatus.REQUESTED).stream()
			.map(SentJoinRequestResponse::from)
			.collect(Collectors.toList());

		return UserInfoResponse.of(myRooms, receivedInvitations, sentJoinRequests);
	}

	@Transactional
	public void updateUserNickname(Long userId, UserNicknameUpdateRequest request) {
		User user = findUserById(userId);
		user.updateNickname(request.nickname());
	}
}
