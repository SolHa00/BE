package server.ourhood.domain.auth.client.kakao;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import server.ourhood.domain.auth.client.kakao.dto.request.KakaoTokenRequest;
import server.ourhood.domain.auth.client.kakao.dto.response.KakaoTokenResponse;
import server.ourhood.domain.auth.client.kakao.error.KakaoTokenProviderErrorHandler;
import server.ourhood.domain.auth.config.properties.KakaoProperties;

import java.nio.charset.StandardCharsets;

@Component
public class KakaoTokenProvider {

	private final RestClient restClient;
	private final KakaoProperties kakaoProperties;

	public KakaoTokenProvider(RestClient.Builder restClientBuilder,
                              KakaoTokenProviderErrorHandler errorHandler,
                              KakaoProperties kakaoProperties) {

		this.restClient = restClientBuilder.clone()
			.defaultStatusHandler(errorHandler)
			.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE,
				StandardCharsets.UTF_8.name())
			.build();
		this.kakaoProperties = kakaoProperties;
	}

	public KakaoTokenResponse fetchAccessToken(String authCode) {
		KakaoTokenRequest request = KakaoTokenRequest.of(kakaoProperties, authCode);
		return restClient.post()
			.uri(kakaoProperties.url().tokenUrl())
			.body(request.toMultiValueMap())
			.retrieve()
			.body(KakaoTokenResponse.class);
	}
}
