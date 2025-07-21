package server.ourhood.domain.image.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ImageType {
	ROOM_THUMBNAIL("room_thumbnail"),
	MOMENT("moment");

	private final String value;
}
