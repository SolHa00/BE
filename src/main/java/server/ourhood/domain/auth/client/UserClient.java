package server.ourhood.domain.auth.client;

import server.ourhood.domain.auth.domain.OAuthType;
import server.ourhood.domain.user.domain.User;

public interface UserClient {

    OAuthType supportType();

    User fetch(AuthContext context);
}
