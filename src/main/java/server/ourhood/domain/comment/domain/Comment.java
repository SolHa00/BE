package server.ourhood.domain.comment.domain;

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
import server.ourhood.domain.moment.domain.Moment;
import server.ourhood.domain.user.domain.User;
import server.ourhood.global.exception.BaseException;

@Entity
@Table(name = "comment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Comment extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "comment_id")
	private Long id;

	@Column(name = "content", nullable = false)
	private String content;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "moment_id")
	private Moment moment;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_id")
	private Comment parent;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "owner_id")
	private User owner;

	@OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Comment> replyComments = new ArrayList<>();

	@Builder
	public Comment(String content, Moment moment, Comment parent, User owner) {
		this.content = content;
		this.moment = moment;
		this.owner = owner;
		this.parent = parent;
	}

	public static Comment createComment(String content, Moment moment, Comment parent, User owner) {
		if (parent != null && parent.isReply()) {
			throw new BaseException(INVALID_COMMENT_LEVEL);
		}
		return Comment.builder()
			.content(content)
			.moment(moment)
			.owner(owner)
			.parent(parent)
			.build();
	}

	public boolean isReply() {
		return this.parent != null;
	}

	public void updateContent(String content) {
		this.content = content;
	}

	public void validateOwner(User user) {
		if (!this.owner.equals(user)) {
			throw new BaseException(NOT_COMMENT_OWNER);
		}
	}
}
