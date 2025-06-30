package server.ourhood.domain.moment.converter;

import server.ourhood.domain.moment.domain.Moment;
import server.ourhood.domain.room.domain.Room;
import server.ourhood.domain.user.domain.User;

public class MomentConverter {
	public static Moment toMoment(String momentImageUrl, String momentDescription, Room room, User user) {
		return Moment.builder()
			.imageUrl(momentImageUrl)
			.momentDescription(momentDescription)
			.room(room)
			.user(user)
			.build();
	}
}
