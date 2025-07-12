package server.ourhood.domain.auth.domain;

public enum OAuthType {

	GOOGLE, KAKAO;

	public static OAuthType fromName(String name) {
		return OAuthType.valueOf(name.toUpperCase());
	}
}
