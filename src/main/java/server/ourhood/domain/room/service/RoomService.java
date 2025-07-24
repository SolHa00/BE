package server.ourhood.domain.room.service;

import static server.ourhood.global.exception.BaseResponseStatus.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import server.ourhood.domain.image.domain.Image;
import server.ourhood.domain.image.service.ImageService;
import server.ourhood.domain.room.domain.Room;
import server.ourhood.domain.room.domain.RoomMembers;
import server.ourhood.domain.room.dto.request.RoomCreateRequest;
import server.ourhood.domain.room.dto.request.RoomUpdateRequest;
import server.ourhood.domain.room.dto.response.RoomCreateResponse;
import server.ourhood.domain.room.repository.RoomRepository;
import server.ourhood.domain.user.domain.User;
import server.ourhood.global.exception.BaseException;

@Service
@RequiredArgsConstructor
public class RoomService {

	private final RoomRepository roomRepository;
	private final ImageService imageService;

	@Transactional(readOnly = true)
	public Room findRoomById(Long roomId) {
		return roomRepository.findById(roomId)
			.orElseThrow(() -> new BaseException(NOT_FOUND_ROOM));
	}

	@Transactional
	public RoomCreateResponse createRoom(User user, RoomCreateRequest request) {
		Image thumbnailImage = imageService.findImageByKey(request.thumbnailImageKey());
		if (thumbnailImage != null) {
			thumbnailImage.activate();
		}
		Room room = Room.createRoom(request.roomName(), request.roomDescription(), thumbnailImage, user);
		room.addRoomMember(user);
		roomRepository.save(room);
		return RoomCreateResponse.of(room.getId());
	}

	@Transactional
	public void updateRoom(User user, Long roomId, RoomUpdateRequest request) {
		Room room = findRoomById(roomId);
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
			newThumbnailImage.activate();
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
		Room room = findRoomById(roomId);
		room.validateRoomHost(user);
		if (room.getThumbnailImage() != null) {
			imageService.deleteImage(room.getThumbnailImage());
		}
		roomRepository.delete(room);
	}

	@Transactional
	public void leave(User user, Long roomId) {
		Room room = findRoomById(roomId);
		if (room.isHost(user)) {
			throw new BaseException(HOST_CANNOT_LEAVE_ROOM);
		}
		RoomMembers memberToRemove = room.getRoomMembers().stream()
			.filter(member -> member.getUser().getId().equals(user.getId()))
			.findFirst()
			.orElseThrow(() -> new BaseException(USER_NOT_IN_ROOM));
		room.getRoomMembers().remove(memberToRemove);
	}
}
