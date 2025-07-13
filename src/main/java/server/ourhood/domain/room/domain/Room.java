package server.ourhood.domain.room.domain;

import static server.ourhood.global.exception.BaseResponseStatus.*;

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
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.ourhood.domain.common.BaseTimeEntity;
import server.ourhood.domain.user.domain.User;
import server.ourhood.global.exception.BaseException;

@Entity
@Table(name = "room")
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

	public void updateDetails(String roomName, String roomDescription) {
		this.roomName = roomName;
		this.roomDescription = roomDescription;
	}

	public void updateThumbnailImageUrl(String thumbnailImageUrl) {
		this.thumbnailImageUrl = thumbnailImageUrl;
	}

	public void validateRoomMember(User user) {
		if (!isMember(user)) {
			throw new BaseException(NOT_ROOM_MEMBER);
		}
	}

	public boolean isMember(User user) {
		return this.roomMembers.stream()
			.anyMatch(member -> member.getUser().getId().equals(user.getId()));
	}

	public void validateRoomHost(User user) {
		if (!isHost(user)) {
			throw new BaseException(NOT_ROOM_HOST);
		}
	}

	public boolean isHost(User user) {
		return this.host.getId().equals(user.getId());
	}
}
