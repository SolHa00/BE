package server.ourhood.domain.room.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.ourhood.domain.common.BaseTimeEntity;
import server.ourhood.domain.user.domain.User;
import server.ourhood.global.exception.BaseException;
import server.ourhood.global.exception.BaseResponseStatus;

@Entity(name = "room")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Room extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "room_id")
	private Long id;

	@Column(name = "room_name", nullable = false)
	private String roomName;

	@Column(name = "room_description")
	private String roomDescription;

	@Column(name = "thumbnail_image_url")
	private String thumbnailImageUrl;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "host_id")
	private User host;

	@OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<RoomMembers> roomMembers = new ArrayList<>();

	@Builder
	public Room(String roomName, String roomDescription, String thumbnailImageUrl, User host) {
		this.roomName = roomName;
		this.roomDescription = roomDescription;
		this.thumbnailImageUrl = thumbnailImageUrl;
		this.host = host;
	}

	public void addRoomMember(User user) {
		RoomMembers roomMembers = RoomMembers.builder()
			.user(user)
			.room(this)
			.build();
		this.roomMembers.add(roomMembers);
	}

	public void update(String roomName, String roomDescription, String thumbnailImageUrl) {
		this.roomName = roomName;
		this.roomDescription = roomDescription;
		this.thumbnailImageUrl = thumbnailImageUrl;
	}

	public void validateRoomMember(User user) {
		if (!isMember(user)) {
			throw new BaseException(BaseResponseStatus.NOT_ROOM_MEMBER);
		}
	}

	public boolean isMember(User user) {
		return this.roomMembers.stream()
			.anyMatch(member -> member.getUser().getId().equals(user.getId()));
	}
}
