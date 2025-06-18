package server.ourhood.domain.auth.client;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import server.ourhood.domain.auth.domain.OAuthType;
import server.ourhood.global.exception.BaseException;
import server.ourhood.global.exception.BaseResponseStatus;

@Component
public class AuthCodeRequestUrlProviderComposite {

	private final Map<OAuthType, AuthCodeRequestUrlProvider> mapping;

	public AuthCodeRequestUrlProviderComposite(Set<AuthCodeRequestUrlProvider> providers) {
		this.mapping = providers.stream()
			.collect(Collectors.toUnmodifiableMap(AuthCodeRequestUrlProvider::supportType, Function.identity()));
	}

	public String provide(OAuthType oauthType) {
		return Optional.ofNullable(mapping.get(oauthType))
			.orElseThrow(() -> new BaseException(BaseResponseStatus.OAUTH_TYPE_NOT_FOUND))
			.provide();
	}
}
