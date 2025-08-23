package server.ourhood.domain.join.application;

import static server.ourhood.global.exception.BaseResponseStatus.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import server.ourhood.domain.join.dao.JoinRequestRepository;
import server.ourhood.domain.join.domain.JoinRequest;
import server.ourhood.domain.join.domain.JoinRequestStatus;
import server.ourhood.domain.join.dto.request.CreateJoinRequestRequest;
import server.ourhood.domain.join.dto.response.CreateJoinRequestResponse;
import server.ourhood.domain.room.application.RoomService;
import server.ourhood.domain.room.dao.RoomRepository;
import server.ourhood.domain.room.domain.Room;
import server.ourhood.domain.user.domain.User;
import server.ourhood.global.exception.BaseException;

@Service
@RequiredArgsConstructor
public class JoinRequestService {

	private final JoinRequestRepository joinRequestRepository;
	private final RoomService roomService;
	private final RoomRepository roomRepository;

	public JoinRequest getByJoinRequestId(Long joinRequestId) {
		return joinRequestRepository.findById(joinRequestId)
			.orElseThrow(() -> new BaseException(NOT_FOUND_JOIN_REQUEST));
	}

	@Transactional
	public CreateJoinRequestResponse createJoinRequest(User requester, CreateJoinRequestRequest request) {
		validateIfAlreadyRoomMember(request.roomId(), requester);
		Room room = roomService.getByRoomId(request.roomId());
		validateIfAlreadyRequested(room, requester);
		JoinRequest joinRequest = JoinRequest.createJoinRequest(room, requester);
		joinRequestRepository.save(joinRequest);
		return new CreateJoinRequestResponse(joinRequest.getId());
	}

	/**
	 * 참여 요청자의 방 멤버 여부 검증
	 */
	private void validateIfAlreadyRoomMember(Long roomId, User requester) {
		if (roomRepository.existsByIdAndRoomMembersUser(roomId, requester)) {
			throw new BaseException(ALREADY_MEMBER_IN_ROOM);
		}
	}

	/**
	 * 중복 참여 요청 검증
	 */
	private void validateIfAlreadyRequested(Room room, User requester) {
		if (joinRequestRepository.existsByRequesterAndRoomAndStatus(requester, room, JoinRequestStatus.REQUESTED)) {
			throw new BaseException(CONFLICT_JOIN_REQUEST);
		}
	}

	@Transactional
	public void accept(User reviewer, Long joinRequestId) {
		JoinRequest joinRequest = getByJoinRequestId(joinRequestId);
		Room room = joinRequest.getRoom();
		validateRoomMember(room.getId(), reviewer);
		joinRequest.accept();
		room.addRoomMember(joinRequest.getRequester());
	}

	private void validateRoomMember(Long roomId, User user) {
		if (!roomRepository.existsByIdAndRoomMembersUser(roomId, user)) {
			throw new BaseException(NOT_ROOM_MEMBER);
		}
	}

	@Transactional
	public void reject(User reviewer, Long joinRequestId) {
		JoinRequest joinRequest = getByJoinRequestId(joinRequestId);
		Room room = joinRequest.getRoom();
		validateRoomMember(room.getId(), reviewer);
		joinRequest.reject();
	}

	@Transactional
	public void cancel(User user, Long joinRequestId) {
		JoinRequest joinRequest = getByJoinRequestId(joinRequestId);
		joinRequest.validateRequester(user);
		joinRequest.cancel();
	}
}
