package server.ourhood.domain.moment.service;

import static server.ourhood.global.exception.BaseResponseStatus.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import server.ourhood.domain.image.domain.Image;
import server.ourhood.domain.image.service.ImageService;
import server.ourhood.domain.moment.domain.Moment;
import server.ourhood.domain.moment.dto.request.MomentCreateRequest;
import server.ourhood.domain.moment.dto.request.MomentUpdateRequest;
import server.ourhood.domain.moment.dto.response.MomentCreateResponse;
import server.ourhood.domain.moment.repository.MomentRepository;
import server.ourhood.domain.room.domain.Room;
import server.ourhood.domain.room.service.RoomService;
import server.ourhood.domain.user.domain.User;
import server.ourhood.global.exception.BaseException;

@Service
@RequiredArgsConstructor
public class MomentService {

	private final MomentRepository momentRepository;
	private final RoomService roomService;
	private final ImageService imageService;

	@Transactional(readOnly = true)
	public Moment findMomentById(Long momentId) {
		return momentRepository.findById(momentId)
			.orElseThrow(() -> new BaseException(NOT_FOUND_MOMENT));
	}

	@Transactional
	public MomentCreateResponse createMoment(User user, MomentCreateRequest request) {
		Room room = roomService.findRoomById(request.roomId());
		Image image = imageService.findImageByKey(request.momentImageKey());
		imageService.activateAndMoveImage(image);
		Moment moment = Moment.createMoment(image, request.momentDescription(), room, user);
		momentRepository.save(moment);
		return MomentCreateResponse.of(moment.getId());
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
		imageService.deleteImage(moment.getImage());
		momentRepository.delete(moment);
	}
}
