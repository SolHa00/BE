package server.ourhood.domain.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.ourhood.domain.auth.domain.OAuthIdentifier;
import server.ourhood.domain.common.BaseTimeEntity;

@Entity
@Table(name = "users", uniqueConstraints = {
	@UniqueConstraint(
		name = "oauth_identifier_unique",
		columnNames = {"oauth_id", "oauth_type"}
	)
})
@EqualsAndHashCode(of = "id", callSuper = false)
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

	@Builder
	public User(OAuthIdentifier oauthIdentifier, String name, String nickname, String email) {
		this.oauthIdentifier = oauthIdentifier;
		this.name = name;
		this.nickname = nickname;
		this.email = email;
	}

	public void updateProfileImage(String profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}
}
