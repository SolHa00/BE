package server.ourhood.domain.user.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.ourhood.domain.auth.domain.OAuthIdentifier;
import server.ourhood.domain.room.domain.RoomMembers;
import server.ourhood.domain.common.BaseTimeEntity;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
@Entity
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Embedded
    private OAuthIdentifier oauthIdentifier;

    @Column
    private String name;

    @Column(unique = true)
    private String nickname;

    @Column(unique = true)
    private String email;

    @Column
    private String profileImageUrl;

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
