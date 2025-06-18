package server.ourhood.domain.auth.client;

import lombok.Getter;

@Getter
public class AuthContext {

	private final String authCode;

	public AuthContext(String authCode) {
		this.authCode = authCode;
	}
}
