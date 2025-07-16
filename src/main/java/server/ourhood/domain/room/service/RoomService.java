package server.ourhood.domain.room.service;

import static server.ourhood.global.exception.BaseResponseStatus.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import server.ourhood.domain.room.domain.Room;
import server.ourhood.domain.room.domain.RoomMembers;
import server.ourhood.domain.room.dto.request.RoomCreateRequest;
import server.ourhood.domain.room.dto.request.RoomUpdateRequest;
import server.ourhood.domain.room.dto.response.RoomCreateResponse;
import server.ourhood.domain.room.repository.RoomRepository;
import server.ourhood.domain.user.domain.User;
import server.ourhood.global.exception.BaseException;
import server.ourhood.global.s3.S3Service;

@Service
@RequiredArgsConstructor
public class RoomService {

	private final RoomRepository roomRepository;
	private final S3Service s3Service;

	@Transactional(readOnly = true)
	public Room findRoomById(Long roomId) {
		return roomRepository.findById(roomId)
			.orElseThrow(() -> new BaseException(NOT_FOUND_ROOM));
	}

	@Transactional
	public RoomCreateResponse createRoom(User host, RoomCreateRequest request, MultipartFile thumbnailImage) {
		String thumbnailImageUrl = null;
		if (thumbnailImage != null && !thumbnailImage.isEmpty()) {
			thumbnailImageUrl = s3Service.upload(thumbnailImage);
		}
		Room room = request.toRoom(thumbnailImageUrl, host);
		room.addRoomMember(host);
		roomRepository.save(room);
		return new RoomCreateResponse(room.getId());
	}

	@Transactional
	public void updateRoom(User user, Long roomId, RoomUpdateRequest request, MultipartFile thumbnailImage) {
		Room room = findRoomById(roomId);
		room.validateRoomHost(user);
		handleImageUpdate(room, request.isImageRemoved(), thumbnailImage);
		room.updateDetails(request.roomName(), request.roomDescription());
	}

	private void handleImageUpdate(Room room, Boolean isImageRemoved, MultipartFile newThumbnailImage) {
		String oldThumbnailImage = room.getThumbnailImageUrl();
		if (newThumbnailImage != null && !newThumbnailImage.isEmpty()) {
			String newThumbnailImageUrl = s3Service.upload(newThumbnailImage);
			if (oldThumbnailImage != null && !oldThumbnailImage.isEmpty()) {
				s3Service.deleteFile(oldThumbnailImage);
			}
			room.updateThumbnailImageUrl(newThumbnailImageUrl);
		} else if (Boolean.TRUE.equals(isImageRemoved)) {
			if (oldThumbnailImage != null && !oldThumbnailImage.isEmpty()) {
				s3Service.deleteFile(oldThumbnailImage);
			}
			room.updateThumbnailImageUrl(null);
		}
	}

	@Transactional
	public void deleteRoom(User user, Long roomId) {
		Room room = findRoomById(roomId);
		room.validateRoomHost(user);
		if (room.getThumbnailImageUrl() != null) {
			s3Service.deleteFile(room.getThumbnailImageUrl());
		}
		roomRepository.delete(room);
	}

	@Transactional
	public void leave(User user, Long roomId) {
		Room room = findRoomById(roomId);
		room.validateRoomHost(user);
		RoomMembers memberToRemove = room.getRoomMembers().stream()
			.filter(member -> member.getUser().getId().equals(user.getId()))
			.findFirst()
			.orElseThrow(() -> new BaseException(USER_NOT_IN_ROOM));
		room.getRoomMembers().remove(memberToRemove);
	}
}
