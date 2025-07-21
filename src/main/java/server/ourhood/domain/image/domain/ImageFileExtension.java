package server.ourhood.domain.image.domain;

import static server.ourhood.global.exception.BaseResponseStatus.*;

import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.Getter;
import server.ourhood.global.exception.BaseException;

@Getter
@AllArgsConstructor
public enum ImageFileExtension {
	JPEG("jpeg"),
	JPG("jpg"),
	PNG("png");

	private final String uploadExtension;

	public static ImageFileExtension of(String uploadExtension) {
		return Arrays.stream(values())
			.filter(
				imageFileExtension ->
					imageFileExtension.uploadExtension.equals(uploadExtension))
			.findFirst()
			.orElseThrow(() -> new BaseException(IMAGE_FILE_EXTENSION_NOT_FOUND));
	}
}
