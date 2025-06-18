package server.ourhood.domain.auth.client.kakao;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import server.ourhood.domain.auth.client.kakao.dto.response.KakaoUserResponse;
import server.ourhood.domain.auth.client.kakao.error.KakaoUserInfoProviderErrorHandler;
import server.ourhood.domain.auth.config.properties.KakaoProperties;

import java.nio.charset.StandardCharsets;

@Component
public class KakaoUserInfoProvider {

	private final RestClient restClient;
	private final KakaoProperties kakaoProperties;

	public KakaoUserInfoProvider(RestClient.Builder restClientBuilder,
                                 KakaoUserInfoProviderErrorHandler errorHandler,
                                 KakaoProperties kakaoProperties) {

		this.restClient = restClientBuilder.clone()
			.defaultStatusHandler(errorHandler)
			.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE,
				StandardCharsets.UTF_8.name())
			.build();
		this.kakaoProperties = kakaoProperties;
	}

	public KakaoUserResponse fetchUserInfo(String accessToken) {
		return restClient.post()
			.uri(kakaoProperties.url().userInfoUrl())
			.header(HttpHeaders.AUTHORIZATION, authHeaderValue(accessToken))
			.retrieve()
			.body(KakaoUserResponse.class);
	}

	private String authHeaderValue(String accessToken) {
		return "Bearer %s".formatted(accessToken);
	}
}
