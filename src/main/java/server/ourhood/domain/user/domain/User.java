package server.ourhood.domain.user.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.ourhood.domain.auth.domain.OAuthIdentifier;
import server.ourhood.domain.common.BaseTimeEntity;
import server.ourhood.domain.room.domain.RoomMembers;

@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long id;

	@Embedded
	private OAuthIdentifier oauthIdentifier;

	@Column(name = "name")
	private String name;

	@Column(name = "profile_image_url")
	private String profileImageUrl;

	@Column(name = "nickname", unique = true)
	private String nickname;

	@Column(name = "email", unique = true, nullable = false)
	private String email;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<RoomMembers> rooms = new ArrayList<>();

	@Builder
	public User(OAuthIdentifier oauthIdentifier, String name, String nickname, String email) {
		this.oauthIdentifier = oauthIdentifier;
		this.name = name;
		this.nickname = nickname;
		this.email = email;
	}
}
