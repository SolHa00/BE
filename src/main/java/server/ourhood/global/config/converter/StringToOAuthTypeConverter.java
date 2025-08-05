package server.ourhood.global.config.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import server.ourhood.domain.auth.domain.OAuthType;

@Component
public class StringToOAuthTypeConverter implements Converter<String, OAuthType> {

	@Override
	public OAuthType convert(String source) {
		return OAuthType.valueOf(source.toUpperCase());
	}
}
