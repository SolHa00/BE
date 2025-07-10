package server.ourhood.domain.join.service;

import static server.ourhood.global.exception.BaseResponseStatus.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import server.ourhood.domain.join.converter.JoinRequestConverter;
import server.ourhood.domain.join.domain.JoinRequest;
import server.ourhood.domain.join.domain.JoinRequestStatus;
import server.ourhood.domain.join.dto.request.JoinRequestCreateRequest;
import server.ourhood.domain.join.dto.response.JoinRequestCreateResponse;
import server.ourhood.domain.join.repository.JoinRequestRepository;
import server.ourhood.domain.room.domain.Room;
import server.ourhood.domain.room.service.RoomService;
import server.ourhood.domain.user.domain.User;
import server.ourhood.global.exception.BaseException;

@Service
@RequiredArgsConstructor
public class JoinRequestService {

	private final JoinRequestRepository joinRequestRepository;
	private final RoomService roomService;

	@Transactional(readOnly = true)
	public JoinRequest findJoinRequestById(Long joinRequestId) {
		return joinRequestRepository.findById(joinRequestId)
			.orElseThrow(() -> new BaseException(NOT_FOUND_JOIN_REQUEST));
	}

	@Transactional
	public JoinRequestCreateResponse createJoinRequest(User requester, JoinRequestCreateRequest request) {
		Room room = roomService.findRoomById(request.roomId());
		validateIfAlreadyRoomMember(room, requester);
		validateIfAlreadyRequested(room, requester);
		JoinRequest joinRequest = JoinRequestConverter.toJoinRequest(requester, room);
		joinRequestRepository.save(joinRequest);
		return new JoinRequestCreateResponse(joinRequest.getId());
	}

	/**
	 * 참여 요청자의 방 멤버 여부 검증
	 */
	private void validateIfAlreadyRoomMember(Room room, User requester) {
		if (room.isMember(requester)) {
			throw new BaseException(ALREADY_MEMBER_IN_ROOM);
		}
	}

	/**
	 * 중복 참여 요청 검증
	 */
	private void validateIfAlreadyRequested(Room room, User requester) {
		if (joinRequestRepository.existsByUserAndRoomAndStatus(requester, room, JoinRequestStatus.REQUESTED)) {
			throw new BaseException(CONFLICT_JOIN_REQUEST);
		}
	}

	@Transactional
	public void accept(User reviewer, Long joinRequestId) {
		JoinRequest joinRequest = findJoinRequestById(joinRequestId);
		Room room = joinRequest.getRoom();
		room.validateRoomMember(reviewer);
		joinRequest.accept();
		room.addRoomMember(joinRequest.getRequester());
	}

	@Transactional
	public void reject(User reviewer, Long joinRequestId) {
		JoinRequest joinRequest = findJoinRequestById(joinRequestId);
		Room room = joinRequest.getRoom();
		room.validateRoomMember(reviewer);
		joinRequest.reject();
	}

	@Transactional
	public void cancel(User user, Long joinRequestId) {
		JoinRequest joinRequest = findJoinRequestById(joinRequestId);
		joinRequest.validateRequester(user);
		joinRequest.cancel();
	}
}
