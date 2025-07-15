package server.ourhood.domain.join.domain;

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
@Table(name = "join_request")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class JoinRequest extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "join_request_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "requester_id")
	private User requester;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "room_id")
	private Room room;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private JoinRequestStatus status;

	@Builder
	public JoinRequest(User requester, Room room) {
		this.requester = requester;
		this.room = room;
		this.status = JoinRequestStatus.REQUESTED;
	}

	public void accept() {
		validateProcessable();
		status = JoinRequestStatus.ACCEPTED;
	}

	public void reject() {
		validateProcessable();
		status = JoinRequestStatus.REJECTED;
	}

	public void cancel() {
		validateProcessable();
		this.status = JoinRequestStatus.CANCELED;
	}

	private void validateProcessable() {
		if (this.status != JoinRequestStatus.REQUESTED) {
			throw new BaseException(ALREADY_PROCESSED_JOIN_REQUEST);
		}
	}

	public void validateRequester(User user) {
		if (!requester.equals(user)) {
			throw new BaseException(NOT_ROOM_REQUESTER);
		}
	}
}
