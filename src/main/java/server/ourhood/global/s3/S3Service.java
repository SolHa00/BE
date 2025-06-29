package server.ourhood.global.s3;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class S3Service {

	private final AmazonS3Client amazonS3Client;

	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	public String upload(MultipartFile multipartFile) {
		String fileName = createFileName(multipartFile.getOriginalFilename());
		ObjectMetadata objectMetadata = new ObjectMetadata();
		objectMetadata.setContentType(multipartFile.getContentType());
		objectMetadata.setContentLength(multipartFile.getSize());
		try (InputStream inputStream = multipartFile.getInputStream()) {
			amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata));
		} catch (IOException e) {
			throw new RuntimeException("Failed to create input stream: " + e.getMessage(), e);
		}
		return "https://" + bucket + ".s3.ap-northeast-2.amazonaws.com/" + fileName;
	}

	private String createFileName(String originalFilename) {
		return UUID.randomUUID().toString().concat(getFileExtension(originalFilename));
	}

	public String getFileExtension(String fileName) {
		try {
			return fileName.substring(fileName.lastIndexOf("."));
		} catch (StringIndexOutOfBoundsException e) {
			throw new RuntimeException("잘못된 형식의 파일");
		}
	}

	public void deleteFile(String fileName) {
		amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, fileName));
	}
}
