package server.ourhood.domain.moment.dto.response;

import java.time.LocalDateTime;

import server.ourhood.domain.moment.domain.Moment;
import server.ourhood.global.annotation.DateFormat;

public record GetMomentResponse(
	MomentMetadata momentMetadata,
	MomentDetail momentDetail
) {
	public record MomentMetadata(
		String momentImageUrl,
		Long userId,
		String nickname,
		@DateFormat
		LocalDateTime createdAt
	) {
		public static MomentMetadata of(String momentImageUrl, Moment moment) {
			return new MomentMetadata(
				momentImageUrl,
				moment.getOwner().getId(),
				moment.getOwner().getNickname(),
				moment.getCreatedAt()
			);
		}
	}

	public record MomentDetail(
		String momentDescription
	) {
		public static MomentDetail of(String momentDescription) {
			return new MomentDetail(momentDescription);
		}
	}
}
