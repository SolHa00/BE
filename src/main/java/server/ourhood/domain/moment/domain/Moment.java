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

	@Column(name = "moment_image_url", nullable = false)
	private String momentImageUrl;

	@Column(name = "moment_description")
	private String momentDescription;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "room_id")
	private Room room;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@OneToMany(mappedBy = "moment", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Comment> comments = new ArrayList<>();

	@Builder
	public Moment(String momentImageUrl, String momentDescription, Room room, User user) {
		this.momentImageUrl = momentImageUrl;
		this.momentDescription = momentDescription;
		this.room = room;
		this.user = user;
	}

	public void updateDescription(String momentDescription) {
		this.momentDescription = momentDescription;
	}

	public void validateOwner(User user) {
		if (!this.user.equals(user)) {
			throw new BaseException(NOT_MOMENT_OWNER);
		}
	}
}
