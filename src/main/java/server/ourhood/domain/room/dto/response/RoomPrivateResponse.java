package server.ourhood.domain.room.dto.response;

import java.util.List;

import server.ourhood.domain.moment.domain.Moment;
import server.ourhood.domain.user.domain.User;

public record RoomPrivateResponse(
	Long numOfNewJoinRequests,
	List<UserInfoResponse> members,
	List<MomentInfoResponse> moments
) {
	public record UserInfoResponse(
		Long userId,
		String nickname
	) {
		public static UserInfoResponse from(User user) {
			return new UserInfoResponse(
				user.getId(),
				user.getNickname()
			);
		}
	}

	public record MomentInfoResponse(
		Long momentId,
		String momentImageUrl
	) {
		public static MomentInfoResponse of(Moment moment, String momentImageUrl) {
			return new MomentInfoResponse(
				moment.getId(),
				momentImageUrl
			);
		}
	}
}
