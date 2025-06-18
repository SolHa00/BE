package server.ourhood.domain.moment.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.ourhood.domain.comment.domain.Comment;
import server.ourhood.domain.room.domain.Room;
import server.ourhood.domain.common.BaseTimeEntity;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Moment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "moment_id")
    private Long id;

    private Long userId;
    private String imageUrl;
    private String momentDescription;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @OneToMany(mappedBy = "moment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @Builder
    public Moment(Long userId, String imageUrl, String momentDescription, Room room) {
        this.userId = userId;
        this.imageUrl = imageUrl;
        this.momentDescription = momentDescription;
        this.room = room;
    }
}
