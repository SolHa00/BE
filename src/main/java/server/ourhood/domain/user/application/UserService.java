package server.ourhood.domain.user.application;

import static server.ourhood.domain.user.dto.response.UserInfoResponse.*;
import static server.ourhood.global.exception.BaseResponseStatus.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import server.ourhood.domain.image.domain.Image;
import server.ourhood.domain.invitation.dao.InvitationRepository;
import server.ourhood.domain.invitation.domain.InvitationStatus;
import server.ourhood.domain.join.dao.JoinRequestRepository;
import server.ourhood.domain.join.domain.JoinRequestStatus;
import server.ourhood.domain.room.dao.RoomRepository;
import server.ourhood.domain.user.dao.UserRepository;
import server.ourhood.domain.user.domain.User;
import server.ourhood.domain.user.dto.request.UserInfoUpdateRequest;
import server.ourhood.domain.user.dto.response.UserInfoResponse;
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

	public User getByUserId(Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new BaseException(NOT_FOUND_USER));
	}

	@Transactional(readOnly = true)
	public UserInfoResponse getUserInfo(User user) {
		MyInfo myInfo = MyInfo.from(user);

		List<MyRooms> myRooms = roomRepository.findAllByMemberWithHostAndThumbnailAndMembers(user).stream()
			.map(room -> {
				Image thumbnailImage = room.getThumbnailImage();
				String thumbnailUrl =
					(thumbnailImage != null) ? cloudFrontUtil.getImageUrl(thumbnailImage.getPermanentFileName()) :
						null;
				return MyRooms.of(room, thumbnailUrl);
			})
			.collect(Collectors.toList());

		List<ReceivedInvitations> receivedInvitations = invitationRepository.findByInviteeAndStatusWithRoomAndHost(
				user, InvitationStatus.REQUESTED).stream()
			.map(ReceivedInvitations::from)
			.collect(Collectors.toList());

		List<SentJoinRequests> sentJoinRequests = joinRequestRepository.findByRequesterAndStatusWithRoom(user,
				JoinRequestStatus.REQUESTED).stream()
			.map(SentJoinRequests::from)
			.collect(Collectors.toList());

		return new UserInfoResponse(myInfo, myRooms, receivedInvitations, sentJoinRequests);
	}

	@Transactional
	public void updateUserInfo(Long userId, UserInfoUpdateRequest request) {
		User user = getByUserId(userId);
		user.updateNickname(request.nickname());
	}
}
