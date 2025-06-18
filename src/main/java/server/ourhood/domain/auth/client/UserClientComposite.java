package server.ourhood.domain.auth.client;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import server.ourhood.domain.auth.domain.OAuthType;
import server.ourhood.domain.user.domain.User;
import server.ourhood.global.exception.BaseException;
import server.ourhood.global.exception.BaseResponseStatus;

@Component
public class UserClientComposite {

	private final Map<OAuthType, UserClient> mapping;

	public UserClientComposite(Set<UserClient> clients) {
		mapping = clients.stream()
			.collect(Collectors.toMap(UserClient::supportType, Function.identity()));
	}

	public User fetch(OAuthType oauthType, AuthContext context) {
		return Optional.ofNullable(mapping.get(oauthType))
			.orElseThrow(() -> new BaseException(BaseResponseStatus.OAUTH_TYPE_NOT_FOUND))
			.fetch(context);
	}

	public User fetch(OAuthType oauthType, String authCode) {
		AuthContext context = new AuthContext(authCode);
		return fetch(oauthType, context);
	}
}
