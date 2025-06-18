package server.ourhood.domain.room.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.ourhood.domain.invitation.domain.Invitation;
import server.ourhood.domain.join.domain.JoinRequest;
import server.ourhood.domain.moment.domain.Moment;
import server.ourhood.domain.common.BaseTimeEntity;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Room extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long id;

    private Long hostId;
    private String roomName;
    private String roomDescription;
    private String thumbnailImageUrl;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoomMembers> roomMembers = new ArrayList<>();

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Moment> moments = new ArrayList<>();

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JoinRequest> joinRequests = new ArrayList<>();

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Invitation> invitations = new ArrayList<>();

    @Builder
    public Room(Long hostId, String roomName, String roomDescription, String thumbnailImageUrl) {
        this.hostId = hostId;
        this.roomName = roomName;
        this.roomDescription = roomDescription;
        this.thumbnailImageUrl = thumbnailImageUrl;
    }
}