package server.ourhood.global.util;

import static com.amazonaws.services.cloudfront.CloudFrontCookieSigner.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.security.PrivateKey;
import java.security.Security;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.Cookie;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CloudFrontUtil {

	private final String keyPairId;
	private final PrivateKey privateKey;
	private final String customDomain;
	private final String serviceDomain;
	private final int cdnExpiry;

	public CloudFrontUtil(
		@Value("${cloud.aws.cloudfront.key-pair-id}") String keyPairId,
		@Value("${cloud.aws.cloudfront.private-key-path}") String privateKeyPath,
		@Value("${cloud.aws.cloudfront.custom-domain}") String customDomain,
		@Value("${cloud.aws.cloudfront.service-domain}") String serviceDomain,
		@Value("${cookie.cdn.expiry}") int cdnExpiry) {
		this.keyPairId = keyPairId;
		this.privateKey = loadPrivateKey(privateKeyPath);
		this.customDomain = customDomain;
		this.serviceDomain = serviceDomain;
		this.cdnExpiry = cdnExpiry;
	}

	public String getImageUrl(String fileName) {
		if (fileName == null || fileName.isBlank()) {
			return null;
		}
		return "https://" + this.customDomain + "/" + fileName;
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
			throw new RuntimeException("개인 키 파일 로딩 실패.");
		} catch (Exception e) {
			// 파일 형식 등이 잘못되어 파싱에 실패한 경우
			log.error("CloudFront 개인 키 파일 파싱에 실패했습니다.", e);
			throw new RuntimeException("개인 키 파싱 실패. 애플리케이션을 시작할 수 없습니다.");
		}
	}

	public List<Cookie> generateSignedCookies() {
		String resourceUrl = "https://" + this.customDomain + "/moment/*";
		Date expirationDate = new Date(System.currentTimeMillis() + cdnExpiry);
		CookiesForCustomPolicy cookiesData = getCookiesForCustomPolicy(resourceUrl, this.privateKey, this.keyPairId,
			expirationDate, null, null);
		return convertToServletCookies(cookiesData);
	}

	private List<Cookie> convertToServletCookies(CookiesForCustomPolicy cookiesData) {
		List<Cookie> servletCookies = new ArrayList<>();

		Cookie policyCookie = new Cookie(cookiesData.getPolicy().getKey(), cookiesData.getPolicy().getValue());
		configureCookie(policyCookie);
		servletCookies.add(policyCookie);

		Cookie signatureCookie = new Cookie(cookiesData.getSignature().getKey(), cookiesData.getSignature().getValue());
		configureCookie(signatureCookie);
		servletCookies.add(signatureCookie);

		Cookie keyPairIdCookie = new Cookie(cookiesData.getKeyPairId().getKey(), cookiesData.getKeyPairId().getValue());
		configureCookie(keyPairIdCookie);
		servletCookies.add(keyPairIdCookie);

		return servletCookies;
	}

	private void configureCookie(Cookie cookie) {
		cookie.setDomain(this.serviceDomain);
		cookie.setPath("/");
		cookie.setSecure(true);
		cookie.setHttpOnly(true);
	}
}
