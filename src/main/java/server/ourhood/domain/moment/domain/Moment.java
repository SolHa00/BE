package server.ourhood.domain.moment.domain;

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
import server.ourhood.domain.comment.domain.Comment;
import server.ourhood.domain.common.BaseTimeEntity;
import server.ourhood.domain.room.domain.Room;
import server.ourhood.domain.user.domain.User;
import server.ourhood.global.exception.BaseException;

@Entity
@Table(name = "moment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Moment extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "moment_id")
	private Long id;

	@Column(name = "image_key", length = 36, nullable = false)
	private String imageKey;

	@Column(name = "description")
	private String description;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "room_id")
	private Room room;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "owner_id")
	private User owner;

	@OneToMany(mappedBy = "moment", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Comment> comments = new ArrayList<>();

	@Builder
	public Moment(String imageKey, String description, Room room, User owner) {
		this.imageKey = imageKey;
		this.description = description;
		this.room = room;
		this.owner = owner;
	}

	public static Moment createMoment(String imageKey, String description, Room room, User owner) {
		return Moment.builder()
			.imageKey(imageKey)
			.description(description)
			.room(room)
			.owner(owner)
			.build();
	}

	public void updateDescription(String description) {
		this.description = description;
	}

	public void validateOwner(User user) {
		if (!this.owner.equals(user)) {
			throw new BaseException(NOT_MOMENT_OWNER);
		}
	}
}
