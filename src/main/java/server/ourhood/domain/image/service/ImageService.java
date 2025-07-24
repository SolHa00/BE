package server.ourhood.domain.image.service;

import static server.ourhood.global.exception.BaseResponseStatus.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.amazonaws.HttpMethod;

import lombok.RequiredArgsConstructor;
import server.ourhood.domain.image.domain.Image;
import server.ourhood.domain.image.domain.ImageFileExtension;
import server.ourhood.domain.image.domain.ImageType;
import server.ourhood.domain.image.dto.request.MomentImageCreateRequest;
import server.ourhood.domain.image.dto.request.RoomThumbnailImageCreateRequest;
import server.ourhood.domain.image.dto.response.PresignedUrlResponse;
import server.ourhood.domain.image.repository.ImageRepository;
import server.ourhood.domain.user.domain.User;
import server.ourhood.global.exception.BaseException;
import server.ourhood.global.util.S3Util;
import server.ourhood.global.util.UUIDGenerator;

@Service
@RequiredArgsConstructor
public class ImageService {

	private final S3Util s3Util;
	private final UUIDGenerator uuidGenerator;
	private final ImageRepository imageRepository;

	@Transactional
	public PresignedUrlResponse createRoomThumbnailPresignedUrl(User user, RoomThumbnailImageCreateRequest request) {
		return createPresignedUrl(user, ImageType.ROOM_THUMBNAIL, request.imageFileExtension());
	}

	private PresignedUrlResponse createPresignedUrl(User user, ImageType imageType,
		ImageFileExtension imageFileExtension) {
		validateImageFileExtension(imageFileExtension);
		String imageKey = uuidGenerator.generateUUID();
		Image image = Image.createImage(imageType, imageKey, imageFileExtension, user);
		imageRepository.save(image);
		String fileName = image.getFileName();
		String presignedUrl = s3Util.getS3PresignedUrl(fileName, HttpMethod.PUT,
			imageFileExtension.getUploadExtension());
		return PresignedUrlResponse.of(imageKey, presignedUrl);
	}

	private void validateImageFileExtension(ImageFileExtension imageFileExtension) {
		if (imageFileExtension == null) {
			throw new BaseException(IMAGE_FILE_EXTENSION_NOT_FOUND);
		}
		try {
			ImageFileExtension.of(imageFileExtension.getUploadExtension());
		} catch (IllegalArgumentException e) {
			throw new BaseException(INVALID_IMAGE_FILE_EXTENSION);
		}
	}

	@Transactional
	public PresignedUrlResponse createMomentPresignedUrl(User user, MomentImageCreateRequest request) {
		return createPresignedUrl(user, ImageType.MOMENT, request.imageFileExtension());
	}

	public Image findImageByKey(String imageKey) {
		if (imageKey == null || imageKey.isBlank()) {
			return null;
		}
		return imageRepository.findByImageKey(imageKey)
			.orElseThrow(() -> new BaseException(NOT_FOUND_IMAGE));
	}

	public void deleteImage(Image image) {
		String fileName = image.getFileName();
		s3Util.deleteS3Object(fileName);
		imageRepository.delete(image);
	}
}
