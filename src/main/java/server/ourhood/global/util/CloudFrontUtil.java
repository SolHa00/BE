package server.ourhood.global.util;

import static com.amazonaws.services.cloudfront.CloudFrontCookieSigner.*;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class CloudFrontUtil {

	private final String keyPairId;
	private final PrivateKey privateKey;
	private final String customDomain;
	private final String serviceDomain;
	private final int cdnExpiry;

	public String getImageUrl(String fileName) {
		if (fileName == null || fileName.isBlank()) {
			return null;
		}
		return "https://" + this.customDomain + "/" + fileName;
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
