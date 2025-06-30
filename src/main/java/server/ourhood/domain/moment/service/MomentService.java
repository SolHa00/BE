package server.ourhood.domain.moment.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import server.ourhood.domain.moment.converter.MomentConverter;
import server.ourhood.domain.moment.domain.Moment;
import server.ourhood.domain.moment.dto.request.MomentCreateRequest;
import server.ourhood.domain.moment.dto.request.MomentUpdateRequest;
import server.ourhood.domain.moment.dto.response.MomentCreateResponse;
import server.ourhood.domain.moment.repository.MomentRepository;
import server.ourhood.domain.room.domain.Room;
import server.ourhood.domain.room.service.RoomService;
import server.ourhood.domain.user.domain.User;
import server.ourhood.global.exception.BaseException;
import server.ourhood.global.exception.BaseResponseStatus;
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
			.orElseThrow(() -> new BaseException(BaseResponseStatus.NOT_FOUND_MOMENT));
	}

	@Transactional
	public MomentCreateResponse createMoment(User user, MomentCreateRequest request) {
		Room room = roomService.findRoomById(request.roomId());
		String momentImageUrl = uploadMomentImage(request.momentImage());
		Moment moment = MomentConverter.toMoment(momentImageUrl, request.momentDescription(), room, user);
		momentRepository.save(moment);
		return new MomentCreateResponse(moment.getId());
	}

	@Transactional
	public void updateMoment(User user, Long momentId, MomentUpdateRequest request) {
		Moment moment = findMomentById(momentId);
		validateMomentOwner(user, moment);
		moment.update(request.momentDescription());
	}

	@Transactional
	public void deleteMoment(User user, Long momentId) {
		Moment moment = findMomentById(momentId);
		validateMomentOwner(user, moment);
		s3Service.deleteFile(moment.getImageUrl());
		momentRepository.delete(moment);
	}

	private void validateMomentOwner(User user, Moment moment) {
		if (!moment.getUser().getId().equals(user.getId())) {
			throw new BaseException(BaseResponseStatus.NOT_MOMENT_OWNER);
		}
	}

	private String uploadMomentImage(MultipartFile image) {
		return s3Service.upload(image);
	}
}
