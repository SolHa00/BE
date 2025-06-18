package server.ourhood.domain.auth.client;

import server.ourhood.domain.auth.domain.OAuthType;

public interface AuthCodeRequestUrlProvider {

	OAuthType supportType();

	String provide();
}
