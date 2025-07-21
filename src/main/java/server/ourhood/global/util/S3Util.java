package server.ourhood.global.util;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;

import lombok.RequiredArgsConstructor;
import server.ourhood.global.exception.BaseException;
import server.ourhood.global.exception.BaseResponseStatus;

@Component
@RequiredArgsConstructor
public class S3Util {

	private final AmazonS3 amazonS3;

	@Value("${cloud.aws.s3.bucket}")
	private String bucketName;

	public String getS3PresignedUrl(String fileName, HttpMethod httpMethod, String fileExtension) {
		GeneratePresignedUrlRequest generatePresignedUrlRequest =
			new GeneratePresignedUrlRequest(bucketName, fileName, httpMethod)
				.withKey(fileName)
				.withContentType("image/" + fileExtension)
				.withExpiration(getPreSignedUrlExpiration());
		return amazonS3.generatePresignedUrl(generatePresignedUrlRequest).toString();
	}

	private Date getPreSignedUrlExpiration() {
		return new Date(System.currentTimeMillis() + 1000 * 60 * 30);
	}

	public void deleteS3Object(String fileName) {
		if (!amazonS3.doesObjectExist(bucketName, fileName)) {
			throw new BaseException(BaseResponseStatus.NOT_FOUND_IMAGE_FILE_IN_S3);
		}
		amazonS3.deleteObject(new DeleteObjectRequest(bucketName, fileName));
	}
}
