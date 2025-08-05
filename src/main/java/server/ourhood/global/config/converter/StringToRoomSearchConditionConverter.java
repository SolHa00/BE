package server.ourhood.global.config.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import server.ourhood.domain.room.dto.request.RoomSearchCondition;

@Component
public class StringToRoomSearchConditionConverter implements Converter<String, RoomSearchCondition> {

	@Override
	public RoomSearchCondition convert(String source) {
		if (source == null) {
			return null;
		}
		return RoomSearchCondition.valueOf(source.toUpperCase());
	}
}
