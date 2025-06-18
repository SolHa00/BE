package server.ourhood.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.ourhood.domain.auth.client.AuthCodeRequestUrlProviderComposite;
import server.ourhood.domain.auth.client.UserClientComposite;
import server.ourhood.domain.auth.domain.OAuthType;
import server.ourhood.domain.auth.dto.TokenDto;
import server.ourhood.domain.auth.dto.request.RefreshTokenRequest;
import server.ourhood.domain.auth.dto.response.AuthResponse;
import server.ourhood.domain.auth.dto.response.OAuthUrlResponse;
import server.ourhood.domain.auth.dto.response.TokenResponse;
import server.ourhood.domain.user.domain.User;
import server.ourhood.domain.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthCodeRequestUrlProviderComposite authCodeComposite;
    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final UserClientComposite userClientComposite;

    public OAuthUrlResponse getAuthCodeRequestUrl(OAuthType oauthType) {
        String url = authCodeComposite.provide(oauthType);
        return new OAuthUrlResponse(url);
    }

    @Transactional
    public AuthResponse loginAndGenerateToken(OAuthType oauthType, String authCode) {
        User savedUser = login(oauthType, authCode);
        TokenDto tokens = tokenProvider.provideTokens(savedUser);
        return new AuthResponse(tokens, savedUser);
    }

    private User login(OAuthType oauthType, String authCode) {
        User user = userClientComposite.fetch(oauthType, authCode);
        return userRepository.findByOauthIdentifier(user.getOauthIdentifier())
                .orElseGet(() -> userRepository.save(user));
    }

    public TokenResponse refreshToken(RefreshTokenRequest request) {
        String refreshToken = request.refreshToken();
        String accessToken = tokenProvider.provideAccessTokenByRefreshToken(refreshToken);
        return new TokenResponse(accessToken, refreshToken);
    }

    public void logout(String refreshToken){
        tokenProvider.invalidateRefreshToken(refreshToken);
    }
}
