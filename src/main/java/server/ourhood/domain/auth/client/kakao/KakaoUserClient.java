package server.ourhood.domain.auth.client.kakao;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import server.ourhood.domain.auth.client.AuthContext;
import server.ourhood.domain.auth.client.UserClient;
import server.ourhood.domain.auth.client.kakao.dto.response.KakaoTokenResponse;
import server.ourhood.domain.auth.client.kakao.dto.response.KakaoUserResponse;
import server.ourhood.domain.auth.domain.OAuthIdentifier;
import server.ourhood.domain.auth.domain.OAuthType;
import server.ourhood.domain.user.domain.User;

@Component
@RequiredArgsConstructor
public class KakaoUserClient implements UserClient {

	private final KakaoTokenProvider kakaoTokenProvider;
	private final KakaoUserInfoProvider kakaoUserInfoProvider;

	@Override
	public OAuthType supportType() {
		return OAuthType.KAKAO;
	}

	@Override
	public User fetch(AuthContext authContext) {
		KakaoTokenResponse tokenResponse = kakaoTokenProvider.fetchAccessToken(authContext.getAuthCode());
		KakaoUserResponse userResponse = kakaoUserInfoProvider.fetchUserInfo(tokenResponse.accessToken());
		return buildUser(userResponse);
	}

	private User buildUser(KakaoUserResponse response) {
		OAuthIdentifier oAuthIdentifier = OAuthIdentifier.builder()
			.oauthId(response.id().toString())
			.oauthType(supportType())
			.build();

		return User.builder()
			.name(response.kakaoAccount().nickname())
			.email(response.kakaoAccount().email())
			.oauthIdentifier(oAuthIdentifier)
			.build();
	}
}
