package server.ourhood.domain.room.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import server.ourhood.domain.room.converter.RoomConverter;
import server.ourhood.domain.room.domain.Room;
import server.ourhood.domain.room.domain.RoomMembers;
import server.ourhood.domain.room.dto.request.RoomCreateRequest;
import server.ourhood.domain.room.dto.request.RoomUpdateRequest;
import server.ourhood.domain.room.dto.response.RoomCreateResponse;
import server.ourhood.domain.room.repository.RoomRepository;
import server.ourhood.domain.user.domain.User;
import server.ourhood.global.exception.BaseException;
import server.ourhood.global.exception.BaseResponseStatus;
import server.ourhood.global.s3.S3Service;

@Service
@RequiredArgsConstructor
public class RoomService {

	private final RoomRepository roomRepository;
	private final S3Service s3Service;

	@Transactional(readOnly = true)
	public Room findRoomById(Long roomId) {
		return roomRepository.findById(roomId)
			.orElseThrow(() -> new BaseException(BaseResponseStatus.NOT_FOUND_ROOM));
	}

	@Transactional
	public RoomCreateResponse createRoom(User host, RoomCreateRequest request) {
		String thumbnailImageUrl = null;
		if (hasRoomThumbnail(request.thumbnail())) {
			thumbnailImageUrl = uploadThumbnailImage(request.thumbnail());
		}
		Room room = RoomConverter.toRoom(request.roomName(), request.roomDescription(), thumbnailImageUrl, host);
		room.addRoomMember(host);
		roomRepository.save(room);
		return new RoomCreateResponse(room.getId());
	}

	@Transactional
	public void updateRoom(User user, Long roomId, RoomUpdateRequest request) {
		Room room = findRoomById(roomId);
		if (!isUserRoomHost(user, room)) {
			throw new BaseException(BaseResponseStatus.NOT_ROOM_HOST);
		}
		String thumbnailImageUrl = room.getThumbnailImageUrl();
		if (hasRoomThumbnail(request.thumbnail())) {
			if (thumbnailImageUrl != null) {
				deleteThumbnailImage(thumbnailImageUrl);
			}
			thumbnailImageUrl = uploadThumbnailImage(request.thumbnail());
		}
		room.update(request.roomName(), request.roomDescription(), thumbnailImageUrl);
	}

	@Transactional
	public void deleteRoom(User user, Long roomId) {
		Room room = findRoomById(roomId);
		if (!isUserRoomHost(user, room)) {
			throw new BaseException(BaseResponseStatus.NOT_ROOM_HOST);
		}
		if (room.getThumbnailImageUrl() != null) {
			deleteThumbnailImage(room.getThumbnailImageUrl());
		}
		roomRepository.delete(room);
	}

	@Transactional
	public void leaveRoom(User user, Long roomId) {
		Room room = findRoomById(roomId);
		if (isUserRoomHost(user, room)) {
			throw new BaseException(BaseResponseStatus.HOST_CANNOT_LEAVE_ROOM);
		}
		RoomMembers memberToRemove = room.getRoomMembers().stream()
			.filter(member -> member.getUser().getId().equals(user.getId()))
			.findFirst()
			.orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NOT_IN_ROOM));
		room.getRoomMembers().remove(memberToRemove);
	}

	private boolean hasRoomThumbnail(MultipartFile thumbnail) {
		return thumbnail != null && !thumbnail.isEmpty();
	}

	private boolean isUserRoomHost(User user, Room room) {
		return room.getHost().getId().equals(user.getId());
	}

	private String uploadThumbnailImage(MultipartFile thumbnailImage) {
		return s3Service.upload(thumbnailImage);
	}

	private void deleteThumbnailImage(String thumbnailImageUrl) {
		String fileName = extractFileNameFromUrl(thumbnailImageUrl);
		s3Service.deleteFile(fileName);
	}

	private String extractFileNameFromUrl(String url) {
		return url.substring(url.lastIndexOf("/") + 1);
	}
}
