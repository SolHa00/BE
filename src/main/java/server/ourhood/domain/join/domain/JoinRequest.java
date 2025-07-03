package server.ourhood.domain.join.domain;

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

@Entity(name = "join_request")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class JoinRequest extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "join_request_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "room_id")
	private Room room;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private JoinRequestStatus status;

	@Builder
	public JoinRequest(User user, Room room) {
		this.user = user;
		this.room = room;
		this.status = JoinRequestStatus.PENDING;
	}

	public void changeStatus(JoinRequestStatus status) {
		this.status = status;
	}
}
