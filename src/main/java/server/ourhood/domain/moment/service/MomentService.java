package server.ourhood.domain.moment.service;

import static server.ourhood.global.exception.BaseResponseStatus.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import server.ourhood.domain.moment.domain.Moment;
import server.ourhood.domain.moment.dto.request.MomentCreateRequest;
import server.ourhood.domain.moment.dto.request.MomentUpdateRequest;
import server.ourhood.domain.moment.dto.response.MomentCreateResponse;
import server.ourhood.domain.moment.repository.MomentRepository;
import server.ourhood.domain.room.domain.Room;
import server.ourhood.domain.room.service.RoomService;
import server.ourhood.domain.user.domain.User;
import server.ourhood.global.exception.BaseException;
import server.ourhood.global.s3.S3Service;

@Service
@RequiredArgsConstructor
public class MomentService {

	private final MomentRepository momentRepository;
	private final RoomService roomService;
	private final S3Service s3Service;

	@Transactional(readOnly = true)
	public Moment findMomentById(Long momentId) {
		return momentRepository.findById(momentId)
			.orElseThrow(() -> new BaseException(NOT_FOUND_MOMENT));
	}

	@Transactional
	public MomentCreateResponse createMoment(User user, MomentCreateRequest request, MultipartFile momentImage) {
		Room room = roomService.findRoomById(request.roomId());
		String momentImageUrl = s3Service.upload(momentImage);
		Moment moment = request.toMoment(momentImageUrl, room, user);
		momentRepository.save(moment);
		return new MomentCreateResponse(moment.getId());
	}

	@Transactional
	public void updateMoment(User user, Long momentId, MomentUpdateRequest request) {
		Moment moment = findMomentById(momentId);
		moment.validateOwner(user);
		moment.updateDescription(request.momentDescription());
	}

	@Transactional
	public void deleteMoment(User user, Long momentId) {
		Moment moment = findMomentById(momentId);
		moment.validateOwner(user);
		s3Service.deleteFile(moment.getMomentImageUrl());
		momentRepository.delete(moment);
	}
}
