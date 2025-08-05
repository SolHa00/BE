package server.ourhood.domain.user.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import server.ourhood.domain.invitation.domain.Invitation;
import server.ourhood.domain.join.domain.JoinRequest;
import server.ourhood.domain.room.domain.Room;
import server.ourhood.domain.user.domain.User;
import server.ourhood.global.annotation.DateFormat;

public record UserInfoResponse(
	MyInfo myInfo,
	List<MyRooms> myRooms,
	List<ReceivedInvitations> receivedInvitations,
	List<SentJoinRequests> sentJoinRequests
) {
	public record MyInfo(
		String email,
		String nickname
	) {
		public static MyInfo from(User user) {
			return new MyInfo(
				user.getEmail(),
				user.getNickname()
			);
		}
	}

	public record MyRooms(
		RoomMetadataResponse roomMetadata,
		RoomDetailResponse roomDetail
	) {
		public static MyRooms of(Room room, String thumbnailUrl) {
			return new MyRooms(
				RoomMetadataResponse.from(room),
				RoomDetailResponse.of(room, thumbnailUrl)
			);
		}

		public record RoomMetadataResponse(
			Long roomId,
			String hostName,
			int numOfMembers,
			@DateFormat
			LocalDateTime createdAt
		) {
			public static RoomMetadataResponse from(Room room) {
				return new RoomMetadataResponse(
					room.getId(),
					room.getHost().getNickname(),
					room.getRoomMembers().size(),
					room.getCreatedAt()
				);
			}
		}

		public record RoomDetailResponse(
			String roomName,
			String thumbnailUrl
		) {
			public static RoomDetailResponse of(Room room, String thumbnailUrl) {
				return new RoomDetailResponse(
					room.getName(),
					thumbnailUrl
				);
			}
		}
	}

	public record ReceivedInvitations(
		Long invitationId,
		@DateFormat
		LocalDateTime createdAt,
		String roomName,
		String hostName
	) {
		public static ReceivedInvitations from(Invitation invitation) {
			return new ReceivedInvitations(
				invitation.getId(),
				invitation.getCreatedAt(),
				invitation.getRoom().getName(),
				invitation.getRoom().getHost().getNickname()
			);
		}
	}

	public record SentJoinRequests(
		Long joinRequestId,
		String roomName,
		@DateFormat
		LocalDateTime createdAt
	) {
		public static SentJoinRequests from(JoinRequest joinRequest) {
			return new SentJoinRequests(
				joinRequest.getId(),
				joinRequest.getRoom().getName(),
				joinRequest.getCreatedAt()
			);
		}
	}
}
