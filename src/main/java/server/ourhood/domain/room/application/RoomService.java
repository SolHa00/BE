package server.ourhood.domain.room.application;

import static server.ourhood.domain.room.dto.response.GetRoomListResponse.RoomList.*;
import static server.ourhood.domain.room.dto.response.GetRoomMomentsResponse.*;
import static server.ourhood.global.exception.BaseResponseStatus.*;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import server.ourhood.domain.image.application.ImageService;
import server.ourhood.domain.image.domain.Image;
import server.ourhood.domain.invitation.dao.InvitationRepository;
import server.ourhood.domain.invitation.domain.InvitationStatus;
import server.ourhood.domain.join.dao.JoinRequestRepository;
import server.ourhood.domain.join.domain.JoinRequest;
import server.ourhood.domain.join.domain.JoinRequestStatus;
import server.ourhood.domain.moment.dao.MomentRepository;
import server.ourhood.domain.moment.domain.Moment;
import server.ourhood.domain.room.dao.RoomRepository;
import server.ourhood.domain.room.domain.Room;
import server.ourhood.domain.room.domain.RoomMembers;
import server.ourhood.domain.room.dto.request.RoomCreateRequest;
import server.ourhood.domain.room.dto.request.RoomSearchCondition;
import server.ourhood.domain.room.dto.request.RoomUpdateRequest;
import server.ourhood.domain.room.dto.response.GetRoomDetailResponse;
import server.ourhood.domain.room.dto.response.GetRoomInvitationResponse;
import server.ourhood.domain.room.dto.response.GetRoomInvitationResponse.RoomInvitation;
import server.ourhood.domain.room.dto.response.GetRoomJoinRequestResponse;
import server.ourhood.domain.room.dto.response.GetRoomJoinRequestResponse.RoomJoinRequest;
import server.ourhood.domain.room.dto.response.GetRoomListResponse;
import server.ourhood.domain.room.dto.response.GetRoomListResponse.RoomList;
import server.ourhood.domain.room.dto.response.GetRoomMembersResponse;
import server.ourhood.domain.room.dto.response.GetRoomMembersResponse.MemberInfo;
import server.ourhood.domain.room.dto.response.GetRoomMomentsResponse;
import server.ourhood.domain.room.dto.response.MemberRoomDetailResponse;
import server.ourhood.domain.room.dto.response.NonMemberRoomDetailResponse;
import server.ourhood.domain.room.dto.response.RoomCreateResponse;
import server.ourhood.domain.room.dto.response.RoomDetailResponse;
import server.ourhood.domain.room.dto.response.RoomMetadataResponse;
import server.ourhood.domain.room.dto.response.UserContextResponse;
import server.ourhood.domain.user.domain.User;
import server.ourhood.global.exception.BaseException;
import server.ourhood.global.util.CloudFrontUtil;

@Service
@RequiredArgsConstructor
public class RoomService {

	private final RoomRepository roomRepository;
	private final ImageService imageService;
	private final JoinRequestRepository joinRequestRepository;
	private final CloudFrontUtil cloudFrontUtil;
	private final InvitationRepository invitationRepository;
	private final MomentRepository momentRepository;

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
		Room room = roomRepository.findByIdWithHost(roomId)
			.orElseThrow(() -> new BaseException(NOT_FOUND_ROOM));
		validateRoomHost(user, room);
		handleImageUpdate(room, request.isImageRemoved(), request.newThumbnailImageKey());
		room.updateDetails(request.roomName(), request.roomDescription());
		roomRepository.save(room);
	}

	private void validateRoomHost(User user, Room room) {
		if (!room.getHost().equals(user)) {
			throw new BaseException(NOT_ROOM_HOST);
		}
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
		Room room = roomRepository.findByIdWithHost(roomId)
			.orElseThrow(() -> new BaseException(NOT_FOUND_ROOM));
		validateRoomHost(user, room);
		if (room.getThumbnailImage() != null) {
			imageService.deleteImage(room.getThumbnailImage());
		}
		roomRepository.delete(room);
	}

	@Transactional
	public void leave(User user, Long roomId) {
		Room room = roomRepository.findByIdWithHost(roomId)
			.orElseThrow(() -> new BaseException(NOT_FOUND_ROOM));
		if (room.getHost().equals(user)) {
			throw new BaseException(HOST_CANNOT_LEAVE_ROOM);
		}
		RoomMembers memberToRemove = room.getRoomMembers().stream()
			.filter(member -> member.getUser().getId().equals(user.getId()))
			.findFirst()
			.orElseThrow(() -> new BaseException(USER_NOT_IN_ROOM));
		room.getRoomMembers().remove(memberToRemove);
	}

	@Transactional(readOnly = true)
	public GetRoomDetailResponse getRoomDetail(User user, Long roomId) {
		boolean isMember = roomRepository.existsByIdAndRoomMembersUser(roomId, user);
		Room room = roomRepository.findByIdWithHostAndThumbnail(roomId)
			.orElseThrow(() -> new BaseException(NOT_FOUND_ROOM));
		String thumbnailImageUrl = getImageUrl(room.getThumbnailImage());
		// 멤버인 경우
		if (isMember) {
			return createMemberRoomResponse(user, room, thumbnailImageUrl);
		}
		// 멤버가 아닌 경우
		return createNonMemberRoomResponse(user, room, thumbnailImageUrl);
	}

	private MemberRoomDetailResponse createMemberRoomResponse(User user, Room room, String thumbnailImageUrl) {
		boolean isHost = room.getHost().equals(user);
		Long numOfNewJoinRequests = joinRequestRepository.countByRoomAndStatus(room, JoinRequestStatus.REQUESTED);
		return new MemberRoomDetailResponse(
			new UserContextResponse(true, isHost, null),
			RoomMetadataResponse.from(room),
			RoomDetailResponse.of(room, thumbnailImageUrl),
			numOfNewJoinRequests
		);
	}

	private NonMemberRoomDetailResponse createNonMemberRoomResponse(User user, Room room, String thumbnailImageUrl) {
		Long sentJoinRequestId = joinRequestRepository.findByRoomAndRequester(room, user)
			.map(JoinRequest::getId)
			.orElse(null);
		return new NonMemberRoomDetailResponse(
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
	public GetRoomMembersResponse getRoomMembersInfo(User user, Long roomId) {
		validateRoomMember(roomId, user);
		Room room = roomRepository.findByIdWithRoomMembersAndUser(roomId)
			.orElseThrow(() -> new BaseException(NOT_FOUND_ROOM));
		List<MemberInfo> memberInfoList = room.getRoomMembers().stream()
			.map(roomMembers -> {
				User member = roomMembers.getUser();
				return MemberInfo.from(member);
			})
			.toList();
		return new GetRoomMembersResponse(memberInfoList);
	}

	private void validateRoomMember(Long roomId, User user) {
		if (!roomRepository.existsByIdAndRoomMembersUser(roomId, user)) {
			throw new BaseException(NOT_ROOM_MEMBER);
		}
	}

	@Transactional(readOnly = true)
	public GetRoomMomentsResponse getRoomMomentsInfo(User user, Long roomId) {
		validateRoomMember(roomId, user);
		Room room = getByRoomId(roomId);
		List<Moment> moments = momentRepository.findAllByRoomWithImageOrderByCreatedAtDesc(room);
		List<MomentInfo> momentInfoList = moments.stream()
			.map(moment -> {
				String momentImageUrl = getImageUrl(moment.getImage());
				return MomentInfo.of(moment, momentImageUrl);
			})
			.toList();
		return new GetRoomMomentsResponse(momentInfoList);
	}

	@Transactional(readOnly = true)
	public GetRoomJoinRequestResponse getRoomJoinRequests(User user, Long roomId) {
		validateRoomMember(roomId, user);
		Room room = getByRoomId(roomId);
		List<RoomJoinRequest> joinRequestList = joinRequestRepository.findAllByRoomAndStatusWithRequester(room,
				JoinRequestStatus.REQUESTED)
			.stream()
			.map(RoomJoinRequest::from)
			.toList();
		return new GetRoomJoinRequestResponse(joinRequestList);
	}

	@Transactional(readOnly = true)
	public GetRoomInvitationResponse getRoomInvitations(User user, Long roomId) {
		validateRoomMember(roomId, user);
		Room room = getByRoomId(roomId);
		List<RoomInvitation> invitationList = invitationRepository.findAllByRoomAndStatusWithInvitee(room,
				InvitationStatus.REQUESTED)
			.stream()
			.map(RoomInvitation::from)
			.toList();
		return new GetRoomInvitationResponse(invitationList);
	}

	@Transactional(readOnly = true)
	public GetRoomListResponse getRooms(RoomSearchCondition condition, String keyword, Sort sort) {
		List<Room> rooms = roomRepository.searchRooms(condition, keyword, sort);
		List<RoomList> roomList = rooms.stream()
			.map(room -> {
				String thumbnailUrl = getImageUrl(room.getThumbnailImage());
				RoomMetadata metadata = RoomMetadata.from(room);
				RoomDetail detail = RoomDetail.of(room, thumbnailUrl);
				return new RoomList(metadata, detail);
			})
			.toList();
		return new GetRoomListResponse(roomList);
	}
}
