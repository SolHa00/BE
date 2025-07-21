package server.ourhood.global.util;

import java.io.IOException;
import java.io.InputStreamReader;
import java.security.PrivateKey;
import java.security.Security;
import java.time.Instant;
import java.util.Date;

import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.amazonaws.services.cloudfront.util.SignerUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CloudFrontUtil {

	private final String distributionDomain;
	private final String keyPairId;
	private final PrivateKey privateKey;

	public CloudFrontUtil(
		@Value("${cloud.aws.cloudfront.distribution-domain}") String distributionDomain,
		@Value("${cloud.aws.cloudfront.key-pair-id}") String keyPairId,
		@Value("${cloud.aws.cloudfront.private-key-path}") String privateKeyPath) {
		this.distributionDomain = distributionDomain;
		this.keyPairId = keyPairId;
		this.privateKey = loadPrivateKey(privateKeyPath);
	}

	public String generateSignedUrl(String objectKey) {
		try {
			String resourcePath = SignerUtils.generateResourcePath(
				SignerUtils.Protocol.https,
				distributionDomain,
				objectKey
			);
			Date expiration = Date.from(Instant.now().plusSeconds(60 * 10));
			return com.amazonaws.services.cloudfront.CloudFrontUrlSigner.getSignedURLWithCannedPolicy(
				resourcePath,
				keyPairId,
				privateKey,
				expiration
			);
		} catch (Exception e) {
			log.error("CloudFront 서명된 URL 생성에 실패했습니다. objectKey: {}", objectKey, e);
			throw new RuntimeException("서명된 URL을 생성할 수 없습니다.");
		}
	}

	// BouncyCastle을 사용한 키 파싱
	private PrivateKey loadPrivateKey(String privateKeyPath) {
		Security.addProvider(new BouncyCastleProvider());
		ClassPathResource resource = new ClassPathResource(privateKeyPath);
		try (PEMParser pemParser = new PEMParser(new InputStreamReader(resource.getInputStream()))) {
			Object pemObject = pemParser.readObject();
			JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");
			if (pemObject instanceof PrivateKeyInfo) {
				return converter.getPrivateKey((PrivateKeyInfo)pemObject);
			}
			if (pemObject instanceof PEMKeyPair) {
				return converter.getPrivateKey(((PEMKeyPair)pemObject).getPrivateKeyInfo());
			}
			throw new IllegalArgumentException("지원하지 않는 개인 키 타입입니다: " + pemObject.getClass().getName());
		} catch (IOException e) {
			// 파일을 찾지 못하거나 읽지 못하는 경우
			log.error("CloudFront 개인 키 파일을 로드할 수 없습니다. 경로: {}", privateKeyPath, e);
			throw new RuntimeException("개인 키 파일 로딩 실패. 애플리케이션을 시작할 수 없습니다.");
		} catch (Exception e) {
			// 파일 형식 등이 잘못되어 파싱에 실패한 경우
			log.error("CloudFront 개인 키 파일 파싱에 실패했습니다. 파일 형식을 확인해주세요.", e);
			throw new RuntimeException("개인 키 파싱 실패. 애플리케이션을 시작할 수 없습니다.");
		}
	}
}