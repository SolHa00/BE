package server.ourhood.domain.join.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import server.ourhood.domain.join.converter.JoinRequestConverter;
import server.ourhood.domain.join.domain.JoinRequest;
import server.ourhood.domain.join.domain.JoinRequestStatus;
import server.ourhood.domain.join.dto.request.JoinRequestCreateRequest;
import server.ourhood.domain.join.dto.request.ProcessJoinRequestRequest;
import server.ourhood.domain.join.dto.response.JoinRequestCreateResponse;
import server.ourhood.domain.join.repository.JoinRequestRepository;
import server.ourhood.domain.room.domain.Room;
import server.ourhood.domain.room.service.RoomService;
import server.ourhood.domain.user.domain.User;
import server.ourhood.global.exception.BaseException;
import server.ourhood.global.exception.BaseResponseStatus;

@Service
@RequiredArgsConstructor
public class JoinRequestService {

	private final JoinRequestRepository joinRequestRepository;
	private final RoomService roomService;

	@Transactional(readOnly = true)
	public JoinRequest findJoinRequestById(Long joinRequestId) {
		return joinRequestRepository.findById(joinRequestId)
			.orElseThrow(() -> new BaseException(BaseResponseStatus.NOT_FOUND_JOIN_REQUEST));
	}

	@Transactional
	public JoinRequestCreateResponse createJoinRequest(User user, JoinRequestCreateRequest request) {
		Room room = roomService.findRoomById(request.roomId());
		validateDuplicateJoinRequest(user, room);
		JoinRequest joinRequest = JoinRequestConverter.toJoinRequest(user, room);
		joinRequestRepository.save(joinRequest);
		return new JoinRequestCreateResponse(joinRequest.getId());
	}

	@Transactional
	public void processJoinRequest(User user, Long joinRequestId, ProcessJoinRequestRequest request) {
		JoinRequest joinRequest = findJoinRequestById(joinRequestId);
		Room room = joinRequest.getRoom();
		validateRoomMember(user, room);
		JoinRequestStatus status = JoinRequestStatus.fromName(request.status());
		joinRequest.changeStatus(status);
		if (status == JoinRequestStatus.ACCEPTED) {
			room.addRoomMember(joinRequest.getUser());
		}
	}

	@Transactional
	public void deleteJoinRequest(User user, Long joinRequestId) {
		JoinRequest joinRequest = findJoinRequestById(joinRequestId);
		validateJoinRequestOwner(user, joinRequest);
		joinRequestRepository.delete(joinRequest);
	}

	/**
	 * 해당 방에 사용자의 참여 요청이 이미 대기 상태로 존재하는지 검증
	 */
	private void validateDuplicateJoinRequest(User user, Room room) {
		if (joinRequestRepository.existsByUserAndRoomAndStatus(user, room, JoinRequestStatus.PENDING)) {
			throw new BaseException(BaseResponseStatus.CONFLICT_JOIN_REQUEST);
		}
	}

	/**
	 * 사용자가 방의 멤버인지 검증
	 */
	private void validateRoomMember(User user, Room room) {
		boolean isMember = room.getRoomMembers().stream()
			.anyMatch(member -> member.getUser().getId().equals(user.getId()));
		if (!isMember) {
			throw new BaseException(BaseResponseStatus.NOT_ROOM_MEMBER);
		}
	}

	/**
	 * 사용자가 참여 요청의 소유자인지 검증
	 */
	private void validateJoinRequestOwner(User user, JoinRequest joinRequest) {
		if (!joinRequest.getUser().getId().equals(user.getId())) {
			throw new BaseException(BaseResponseStatus.NOT_JOIN_REQUEST_OWNER);
		}
	}
}
