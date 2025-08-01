package server.ourhood.domain.room.service;

import static server.ourhood.global.exception.BaseResponseStatus.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import server.ourhood.domain.image.domain.Image;
import server.ourhood.domain.image.service.ImageService;
import server.ourhood.domain.invitation.domain.InvitationStatus;
import server.ourhood.domain.invitation.repository.InvitationRepository;
import server.ourhood.domain.join.domain.JoinRequest;
import server.ourhood.domain.join.domain.JoinRequestStatus;
import server.ourhood.domain.join.repository.JoinRequestRepository;
import server.ourhood.domain.moment.repository.MomentRepository;
import server.ourhood.domain.room.domain.Room;
import server.ourhood.domain.room.domain.RoomMembers;
import server.ourhood.domain.room.dto.request.RoomCreateRequest;
import server.ourhood.domain.room.dto.request.RoomUpdateRequest;
import server.ourhood.domain.room.dto.response.GetRoomInvitationResponse;
import server.ourhood.domain.room.dto.response.GetRoomInvitationResponse.RoomInvitation;
import server.ourhood.domain.room.dto.response.GetRoomJoinRequestResponse;
import server.ourhood.domain.room.dto.response.GetRoomJoinRequestResponse.RoomJoinRequest;
import server.ourhood.domain.room.dto.response.GetRoomResponse;
import server.ourhood.domain.room.dto.response.MemberRoomResponse;
import server.ourhood.domain.room.dto.response.NonMemberRoomResponse;
import server.ourhood.domain.room.dto.response.RoomCreateResponse;
import server.ourhood.domain.room.dto.response.RoomDetailResponse;
import server.ourhood.domain.room.dto.response.RoomMetadataResponse;
import server.ourhood.domain.room.dto.response.RoomPrivateResponse;
import server.ourhood.domain.room.dto.response.RoomPrivateResponse.MomentInfoResponse;
import server.ourhood.domain.room.dto.response.RoomPrivateResponse.UserInfoResponse;
import server.ourhood.domain.room.dto.response.UserContextResponse;
import server.ourhood.domain.room.repository.RoomRepository;
import server.ourhood.domain.user.domain.User;
import server.ourhood.global.exception.BaseException;
import server.ourhood.global.util.CloudFrontUtil;

@Service
@RequiredArgsConstructor
public class RoomService {

	private final RoomRepository roomRepository;
	private final ImageService imageService;
	private final JoinRequestRepository joinRequestRepository;
	private final MomentRepository momentRepository;
	private final CloudFrontUtil cloudFrontUtil;
	private final InvitationRepository invitationRepository;

	public Room getByRoomId(Long roomId) {
		return roomRepository.findById(roomId)
			.orElseThrow(() -> new BaseException(NOT_FOUND_ROOM));
	}

	@Transactional
	public RoomCreateResponse createRoom(User user, RoomCreateRequest request) {
		Image thumbnailImage = imageService.findImageByKey(request.thumbnailImageKey());
		if (thumbnailImage != null) {
			imageService.activateAndMoveImage(thumbnailImage);
		}
		Room room = Room.createRoom(request.roomName(), request.roomDescription(), thumbnailImage, user);
		room.addRoomMember(user);
		roomRepository.save(room);
		return new RoomCreateResponse(room.getId());
	}

	@Transactional
	public void updateRoom(User user, Long roomId, RoomUpdateRequest request) {
		Room room = getByRoomId(roomId);
		room.validateRoomHost(user);
		handleImageUpdate(room, request.isImageRemoved(), request.newThumbnailImageKey());
		room.updateDetails(request.roomName(), request.roomDescription());
		roomRepository.save(room);
	}

	private void handleImageUpdate(Room room, Boolean imageRemoved, String newThumbnailImageKey) {
		Image oldThumbnailImage = room.getThumbnailImage();
		if (newThumbnailImageKey != null && !newThumbnailImageKey.isBlank()) {
			// Case 1: 새로운 썸네일 이미지를 설정하는 경우
			if (oldThumbnailImage != null) {
				imageService.deleteImage(oldThumbnailImage);
			}
			Image newThumbnailImage = imageService.findImageByKey(newThumbnailImageKey);
			imageService.activateAndMoveImage(newThumbnailImage);
			room.updateThumbnailImage(newThumbnailImage);
		} else if (Boolean.TRUE.equals(imageRemoved)) {
			// Case 2: 기존 썸네일 이미지를 제거하는 경우
			if (oldThumbnailImage != null) {
				imageService.deleteImage(oldThumbnailImage);
			}
			room.updateThumbnailImage(null);
		}
		// Case 3: 이미지 변경 없음
	}

	@Transactional
	public void deleteRoom(User user, Long roomId) {
		Room room = getByRoomId(roomId);
		room.validateRoomHost(user);
		if (room.getThumbnailImage() != null) {
			imageService.deleteImage(room.getThumbnailImage());
		}
		roomRepository.delete(room);
	}

	@Transactional
	public void leave(User user, Long roomId) {
		Room room = getByRoomId(roomId);
		if (room.isHost(user)) {
			throw new BaseException(HOST_CANNOT_LEAVE_ROOM);
		}
		RoomMembers memberToRemove = room.getRoomMembers().stream()
			.filter(member -> member.getUser().getId().equals(user.getId()))
			.findFirst()
			.orElseThrow(() -> new BaseException(USER_NOT_IN_ROOM));
		room.getRoomMembers().remove(memberToRemove);
	}

	@Transactional(readOnly = true)
	public GetRoomResponse getRoomInfo(User user, Long roomId) {
		boolean isMember = roomRepository.existsByIdAndRoomMembersUser(roomId, user);
		// 멤버인 경우, 모든 상세 정보 조회
		if (isMember) {
			Room room = roomRepository.findByIdWithAllDetails(roomId)
				.orElseThrow(() -> new BaseException(NOT_FOUND_ROOM));
			String thumbnailImageUrl = getImageUrl(room.getThumbnailImage());
			return createMemberRoomResponse(user, room, thumbnailImageUrl);
		}
		// 멤버가 아닌 경우, 공개된 정보만 조회
		Room room = roomRepository.findByIdWithHostAndThumbnail(roomId)
			.orElseThrow(() -> new BaseException(NOT_FOUND_ROOM));
		String thumbnailImageUrl = getImageUrl(room.getThumbnailImage());
		return createNonMemberRoomResponse(user, room, thumbnailImageUrl);
	}

	private MemberRoomResponse createMemberRoomResponse(User user, Room room, String thumbnailImageUrl) {
		boolean isHost = room.isHost(user);
		Long numOfNewJoinRequests = joinRequestRepository.countByRoomAndStatus(room, JoinRequestStatus.REQUESTED);

		List<UserInfoResponse> members = room.getRoomMembers().stream()
			.map(RoomMembers::getUser)
			.map(UserInfoResponse::from)
			.toList();

		List<MomentInfoResponse> moments = momentRepository.findAllByRoomWithImage(room).stream()
			.map(moment -> MomentInfoResponse.of(moment, getImageUrl(moment.getImage())))
			.toList();

		RoomPrivateResponse privateResponse = new RoomPrivateResponse(numOfNewJoinRequests, members, moments);

		return new MemberRoomResponse(
			new UserContextResponse(true, isHost, null),
			RoomMetadataResponse.from(room),
			RoomDetailResponse.of(room, thumbnailImageUrl),
			privateResponse
		);
	}

	private NonMemberRoomResponse createNonMemberRoomResponse(User user, Room room, String thumbnailImageUrl) {
		Long sentJoinRequestId = joinRequestRepository.findByRoomAndRequester(room, user)
			.map(JoinRequest::getId)
			.orElse(null);

		return new NonMemberRoomResponse(
			new UserContextResponse(false, false, sentJoinRequestId),
			RoomMetadataResponse.from(room),
			RoomDetailResponse.of(room, thumbnailImageUrl)
		);
	}

	private String getImageUrl(Image image) {
		if (image == null) {
			return null;
		}
		return cloudFrontUtil.getImageUrl(image.getPermanentFileName());
	}

	@Transactional(readOnly = true)
	public GetRoomJoinRequestResponse getRoomJoinRequests(User user, Long roomId) {
		Room room = getByRoomId(roomId);
		room.validateRoomMember(user);
		List<RoomJoinRequest> joinRequestList = joinRequestRepository.findByRoomAndStatusWithRequester(room,
				JoinRequestStatus.REQUESTED)
			.stream()
			.map(RoomJoinRequest::from)
			.toList();
		return new GetRoomJoinRequestResponse(joinRequestList);
	}

	@Transactional(readOnly = true)
	public GetRoomInvitationResponse getRoomInvitations(User user, Long roomId) {
		Room room = getByRoomId(roomId);
		room.validateRoomMember(user);
		List<RoomInvitation> invitationList = invitationRepository.findByRoomAndStatusWithInvitee(room,
				InvitationStatus.REQUESTED)
			.stream()
			.map(RoomInvitation::from)
			.toList();
		return new GetRoomInvitationResponse(invitationList);
	}
}
