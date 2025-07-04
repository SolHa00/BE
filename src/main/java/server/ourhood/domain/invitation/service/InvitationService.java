package server.ourhood.domain.invitation.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import server.ourhood.domain.invitation.converter.InvitationConverter;
import server.ourhood.domain.invitation.domain.Invitation;
import server.ourhood.domain.invitation.domain.InvitationStatus;
import server.ourhood.domain.invitation.dto.request.InvitationCreateRequest;
import server.ourhood.domain.invitation.dto.request.ProcessInvitationRequest;
import server.ourhood.domain.invitation.dto.response.InvitationCreateResponse;
import server.ourhood.domain.invitation.repository.InvitationRepository;
import server.ourhood.domain.room.domain.Room;
import server.ourhood.domain.room.service.RoomService;
import server.ourhood.domain.user.domain.User;
import server.ourhood.domain.user.service.UserService;
import server.ourhood.global.exception.BaseException;
import server.ourhood.global.exception.BaseResponseStatus;

@Service
@RequiredArgsConstructor
public class InvitationService {

	private final InvitationRepository invitationRepository;
	private final UserService userService;
	private final RoomService roomService;

	@Transactional(readOnly = true)
	public Invitation findInvitationById(Long invitationId) {
		return invitationRepository.findById(invitationId)
			.orElseThrow(() -> new BaseException(BaseResponseStatus.NOT_FOUND_INVITATION));
	}

	@Transactional
	public InvitationCreateResponse createInvitation(User inviter, InvitationCreateRequest request) {
		Room room = roomService.findRoomById(request.roomId());
		validateRoomMember(inviter, room);
		User invitedUser = userService.findUserByNickname(request.nickname());
		validateDuplicateInvitation(invitedUser, room);
		Invitation invitation = InvitationConverter.toInvitation(invitedUser, room);
		invitationRepository.save(invitation);
		return new InvitationCreateResponse(invitation.getId());
	}

	@Transactional
	public void processInvitation(User invitedUser, Long invitationId, ProcessInvitationRequest request) {
		Invitation invitation = findInvitationById(invitationId);
		validateInvitedUser(invitedUser, invitation);
		InvitationStatus status = InvitationStatus.fromName(request.status());
		invitation.changeStatus(status);
		if (status == InvitationStatus.ACCEPTED) {
			Room room = invitation.getRoom();
			User user = invitation.getUser();
			room.addRoomMember(user);
		}
	}

	@Transactional
	public void deleteInvitation(User inviter, Long invitationId) {
		Invitation invitation = findInvitationById(invitationId);
		Room room = invitation.getRoom();
		validateRoomMember(inviter, room);
		invitationRepository.delete(invitation);
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
	 * 중복 초대 요청 검증
	 */
	private void validateDuplicateInvitation(User invitedUser, Room room) {
		if (invitationRepository.existsByUserAndRoomAndStatus(invitedUser, room, InvitationStatus.PENDING)) {
			throw new BaseException(BaseResponseStatus.CONFLICT_INVITATION);
		}
	}

	/**
	 * 초대받은 사용자가 맞는지 검증
	 */
	private void validateInvitedUser(User invitedUser, Invitation invitation) {
		if (!invitation.getUser().getId().equals(invitedUser.getId())) {
			throw new BaseException(BaseResponseStatus.NOT_ROOM_INVITER);
		}
	}
}
