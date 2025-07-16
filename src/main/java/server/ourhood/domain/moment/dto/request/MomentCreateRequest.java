package server.ourhood.domain.moment.dto.request;

import server.ourhood.domain.moment.domain.Moment;
import server.ourhood.domain.room.domain.Room;
import server.ourhood.domain.user.domain.User;

public record MomentCreateRequest(
	Long roomId,
	String momentDescription
) {
	public Moment toMoment(String momentImageUrl, Room room, User user) {
		return Moment.builder()
			.momentImageUrl(momentImageUrl)
			.momentDescription(this.momentDescription)
			.room(room)
			.user(user)
			.build();
	}
}
