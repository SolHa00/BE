package server.ourhood.domain.invitation.domain;

import static server.ourhood.global.exception.BaseResponseStatus.*;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.ourhood.domain.common.BaseTimeEntity;
import server.ourhood.domain.room.domain.Room;
import server.ourhood.domain.user.domain.User;
import server.ourhood.global.exception.BaseException;

@Entity(name = "invitation")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Invitation extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "invitation_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "invitee_id")
	private User invitee;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "room_id")
	private Room room;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private InvitationStatus status;

	@Builder
	public Invitation(User invitee, Room room) {
		this.invitee = invitee;
		this.room = room;
		this.status = InvitationStatus.REQUESTED;
	}

	public void accept() {
		validateAccept(status);
		status = InvitationStatus.ACCEPTED;
	}

	private void validateAccept(InvitationStatus status) {
		if (status.isCanceled() || status.isRejected()) {
			throw new BaseException(ALREADY_PROCESSED_INVITATION);
		}
	}

	public void reject() {
		validateReject(status);
		status = InvitationStatus.REJECTED;
	}

	private void validateReject(InvitationStatus status) {
		if (status.isCanceled() || status.isAccepted()) {
			throw new BaseException(ALREADY_PROCESSED_INVITATION);
		}
	}

	public void cancel() {
		validateCancel(status);
		this.status = InvitationStatus.CANCELED;
	}

	private void validateCancel(InvitationStatus status) {
		if (status.isAccepted() || status.isRejected()) {
			throw new BaseException(ALREADY_PROCESSED_INVITATION);
		}
	}

	public void validateInvitee(User user) {
		if (!invitee.equals(user)) {
			throw new BaseException(NOT_ROOM_INVITER);
		}
	}
}
