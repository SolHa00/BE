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
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.ourhood.domain.common.BaseTimeEntity;
import server.ourhood.domain.room.domain.Room;
import server.ourhood.domain.user.domain.User;
import server.ourhood.global.exception.BaseException;

@Entity
@Table(name = "invitation")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Invitation extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "invitation_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "room_id")
	private Room room;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "invitee_id")
	private User invitee;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private InvitationStatus status;

	@Builder
	public Invitation(Room room, User invitee) {
		this.room = room;
		this.invitee = invitee;
		this.status = InvitationStatus.REQUESTED;
	}

	public static Invitation createInvitation(Room room, User invitee) {
		return Invitation.builder()
			.room(room)
			.invitee(invitee)
			.build();
	}

	public void accept() {
		validateProcessable();
		status = InvitationStatus.ACCEPTED;
	}

	public void reject() {
		validateProcessable();
		status = InvitationStatus.REJECTED;
	}

	public void cancel() {
		validateProcessable();
		this.status = InvitationStatus.CANCELED;
	}

	private void validateProcessable() {
		if (this.status != InvitationStatus.REQUESTED) {
			throw new BaseException(ALREADY_PROCESSED_INVITATION);
		}
	}

	public void validateInvitee(User user) {
		if (!invitee.equals(user)) {
			throw new BaseException(NOT_ROOM_INVITER);
		}
	}
}
