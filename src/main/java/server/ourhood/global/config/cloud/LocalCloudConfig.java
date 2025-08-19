package server.ourhood.global.config.cloud;

import java.io.IOException;
import java.io.InputStreamReader;
import java.security.PrivateKey;
import java.security.Security;

import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;

import lombok.extern.slf4j.Slf4j;
import server.ourhood.global.util.CloudFrontUtil;

@Slf4j
@Profile("local")
@Configuration
public class LocalCloudConfig {

	@Value("${cloud.aws.cloudfront.key-pair-id}")
	private String keyPairId;
	@Value("${cloud.aws.cloudfront.private-key-path}")
	private String privateKeyPath;
	@Value("${cloud.aws.cloudfront.custom-domain}")
	private String customDomain;
	@Value("${cloud.aws.cloudfront.service-domain}")
	private String serviceDomain;
	@Value("${cookie.cdn.expiry}")
	private int cdnExpiry;

	@Bean
	public CloudFrontUtil cloudFrontUtil() {
		PrivateKey privateKey = loadPrivateKey(privateKeyPath);
		return new CloudFrontUtil(keyPairId, privateKey, customDomain, serviceDomain, cdnExpiry * 1000);
	}

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
			log.error("CloudFront 개인 키 파일을 로드할 수 없습니다. 경로: {}", privateKeyPath, e);
			throw new RuntimeException("개인 키 파일 로딩 실패.");
		}
	}
}
