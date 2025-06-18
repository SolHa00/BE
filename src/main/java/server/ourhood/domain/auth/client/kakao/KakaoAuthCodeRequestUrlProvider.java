package server.ourhood.domain.auth.client.kakao;

import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.RequiredArgsConstructor;
import server.ourhood.domain.auth.client.AuthCodeRequestUrlProvider;
import server.ourhood.domain.auth.config.properties.KakaoProperties;
import server.ourhood.domain.auth.domain.OAuthType;

@Component
@RequiredArgsConstructor
public class KakaoAuthCodeRequestUrlProvider implements AuthCodeRequestUrlProvider {

	private final KakaoProperties kakaoProperties;

	@Override
	public OAuthType supportType() {
		return OAuthType.KAKAO;
	}

	@Override
	public String provide() {
		return UriComponentsBuilder.fromUriString(kakaoProperties.url().authCodeUrl())
			.queryParam("client_id", kakaoProperties.clientId())
			.queryParam("redirect_uri", kakaoProperties.redirectUri())
			.queryParam("response_type", "code")
			.toUriString();
	}
}
