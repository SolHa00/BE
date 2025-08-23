package server.ourhood.domain.invitation.application;

import static server.ourhood.global.exception.BaseResponseStatus.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import server.ourhood.domain.invitation.dao.InvitationRepository;
import server.ourhood.domain.invitation.domain.Invitation;
import server.ourhood.domain.invitation.domain.InvitationStatus;
import server.ourhood.domain.invitation.dto.request.CreateInvitationRequest;
import server.ourhood.domain.invitation.dto.response.CreateInvitationResponse;
import server.ourhood.domain.room.application.RoomService;
import server.ourhood.domain.room.dao.RoomRepository;
import server.ourhood.domain.room.domain.Room;
import server.ourhood.domain.user.dao.UserRepository;
import server.ourhood.domain.user.domain.User;
import server.ourhood.global.exception.BaseException;

@Service
@RequiredArgsConstructor
public class InvitationService {

	private final InvitationRepository invitationRepository;
	private final UserRepository userRepository;
	private final RoomService roomService;
	private final RoomRepository roomRepository;

	public Invitation getByInvitationId(Long invitationId) {
		return invitationRepository.findById(invitationId)
			.orElseThrow(() -> new BaseException(NOT_FOUND_INVITATION));
	}

	@Transactional
	public CreateInvitationResponse createInvitation(User inviter, CreateInvitationRequest request) {
		validateRoomMember(request.roomId(), inviter);
		User invitee = userRepository.findByNickname(request.nickname())
			.orElseThrow(() -> new BaseException(NOT_FOUND_USER));
		validateIfAlreadyRoomMember(request.roomId(), invitee);
		Room room = roomService.getByRoomId(request.roomId());
		validateIfAlreadyRequested(room, invitee);
		Invitation invitation = Invitation.createInvitation(room, invitee);
		invitationRepository.save(invitation);
		return new CreateInvitationResponse(invitation.getId());
	}

	private void validateRoomMember(Long roomId, User user) {
		if (!roomRepository.existsByIdAndRoomMembersUser(roomId, user)) {
			throw new BaseException(NOT_ROOM_MEMBER);
		}
	}

	/**
	 * 초대 대상의 방 멤버 여부 검증
	 */
	private void validateIfAlreadyRoomMember(Long roomId, User invitee) {
		if (roomRepository.existsByIdAndRoomMembersUser(roomId, invitee)) {
			throw new BaseException(ALREADY_MEMBER_IN_ROOM);
		}
	}

	/**
	 * 중복 초대 요청 검증
	 */
	private void validateIfAlreadyRequested(Room room, User invitee) {
		if (invitationRepository.existsByInviteeAndRoomAndStatus(invitee, room, InvitationStatus.REQUESTED)) {
			throw new BaseException(CONFLICT_INVITATION);
		}
	}

	@Transactional
	public void accept(User invitee, Long invitationId) {
		Invitation invitation = getByInvitationId(invitationId);
		invitation.validateInvitee(invitee);
		invitation.accept();
		Room room = invitation.getRoom();
		User user = invitation.getInvitee();
		room.addRoomMember(user);
	}

	@Transactional
	public void reject(User invitee, Long invitationId) {
		Invitation invitation = getByInvitationId(invitationId);
		invitation.validateInvitee(invitee);
		invitation.reject();
	}

	@Transactional
	public void cancel(User inviter, Long invitationId) {
		Invitation invitation = getByInvitationId(invitationId);
		Room room = invitation.getRoom();
		validateRoomMember(room.getId(), inviter);
		invitation.cancel();
	}
}
