package server.ourhood.domain.invitation.service;

import static server.ourhood.global.exception.BaseResponseStatus.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import server.ourhood.domain.invitation.domain.Invitation;
import server.ourhood.domain.invitation.domain.InvitationStatus;
import server.ourhood.domain.invitation.dto.request.InvitationCreateRequest;
import server.ourhood.domain.invitation.dto.response.InvitationCreateResponse;
import server.ourhood.domain.invitation.repository.InvitationRepository;
import server.ourhood.domain.room.domain.Room;
import server.ourhood.domain.room.service.RoomService;
import server.ourhood.domain.user.domain.User;
import server.ourhood.domain.user.service.UserService;
import server.ourhood.global.exception.BaseException;

@Service
@RequiredArgsConstructor
public class InvitationService {

	private final InvitationRepository invitationRepository;
	private final UserService userService;
	private final RoomService roomService;

	@Transactional(readOnly = true)
	public Invitation findInvitationById(Long invitationId) {
		return invitationRepository.findById(invitationId)
			.orElseThrow(() -> new BaseException(NOT_FOUND_INVITATION));
	}

	@Transactional
	public InvitationCreateResponse createInvitation(User inviter, InvitationCreateRequest request) {
		Room room = roomService.findRoomById(request.roomId());
		room.validateRoomMember(inviter);
		User invitee = userService.findUserByNickname(request.nickname());
		validateIfAlreadyRoomMember(room, invitee);
		validateIfAlreadyRequested(room, invitee);
		Invitation invitation = Invitation.createInvitation(room, invitee);
		invitationRepository.save(invitation);
		return InvitationCreateResponse.of(invitation.getId());
	}

	/**
	 * 초대 대상의 방 멤버 여부 검증
	 */
	private void validateIfAlreadyRoomMember(Room room, User invitee) {
		if (room.isMember(invitee)) {
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
		Invitation invitation = findInvitationById(invitationId);
		invitation.validateInvitee(invitee);
		invitation.accept();
		Room room = invitation.getRoom();
		User user = invitation.getInvitee();
		room.addRoomMember(user);
	}

	@Transactional
	public void reject(User invitee, Long invitationId) {
		Invitation invitation = findInvitationById(invitationId);
		invitation.validateInvitee(invitee);
		invitation.reject();
	}

	@Transactional
	public void cancel(User inviter, Long invitationId) {
		Invitation invitation = findInvitationById(invitationId);
		Room room = invitation.getRoom();
		room.validateRoomMember(inviter);
		invitation.cancel();
	}
}
