package server.ourhood.domain.room.dto.response;

import java.util.List;

import server.ourhood.domain.moment.domain.Moment;

public record GetRoomMomentsResponse(
	List<MomentInfo> moments
) {
	public record MomentInfo(
		Long momentId,
		String momentImageUrl
	) {
		public static MomentInfo of(Moment moment, String momentImageUrl) {
			return new MomentInfo(
				moment.getId(),
				momentImageUrl
			);
		}
	}
}
