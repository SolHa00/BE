package server.ourhood.domain.image.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.ourhood.domain.common.BaseTimeEntity;
import server.ourhood.domain.user.domain.User;

@Entity
@Table(name = "image")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Image extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "image_id")
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(name = "image_type")
	private ImageType imageType;

	@Column(name = "image_key", length = 36, nullable = false, unique = true)
	private String imageKey;

	@Enumerated(EnumType.STRING)
	@Column(name = "image_file_extension")
	private ImageFileExtension imageFileExtension;

	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private ImageStatus status;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "owner_id")
	private User owner;

	@Builder
	private Image(ImageType imageType, String imageKey, ImageFileExtension imageFileExtension, User owner) {
		this.imageType = imageType;
		this.imageKey = imageKey;
		this.imageFileExtension = imageFileExtension;
		this.owner = owner;
		this.status = ImageStatus.PENDING;
	}

	public static Image createImage(ImageType imageType, String imageKey, ImageFileExtension imageFileExtension,
		User owner) {
		return Image.builder()
			.imageType(imageType)
			.imageKey(imageKey)
			.imageFileExtension(imageFileExtension)
			.owner(owner)
			.build();
	}

	public void activate() {
		this.status = ImageStatus.ACTIVE;
	}

	public String getFileName() {
		return String.format(
			"%s/%d/%s.%s",
			this.imageType.getValue(),
			this.owner.getId(),
			this.imageKey,
			this.imageFileExtension.getUploadExtension());
	}
}
