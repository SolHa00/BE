package server.ourhood.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import server.ourhood.domain.auth.config.properties.TokenProperties;
import server.ourhood.global.exception.BaseException;
import server.ourhood.global.exception.BaseResponseStatus;
import server.ourhood.global.redis.RedisService;

import java.time.Duration;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UuidTokenProvider {

    private final TokenProperties tokenProperties;
    private final RedisService redisService;

    public String generateRefreshToken(String userId) {
        String refreshToken = UUID.randomUUID().toString();
        Duration expiration = Duration.ofSeconds(tokenProperties.refreshTokenExpirationTime());
        redisService.setValueWithTTL(refreshToken, userId, expiration);
        return refreshToken;
    }

    public String getUserId(String token) {
        Object userId = redisService.getValue(token);
        if (userId == null) {
            throw new BaseException(BaseResponseStatus.EXPIRED_REFRESH_TOKEN);
        }
        return userId.toString();
    }

    public void revokeRefreshToken(String token) {
        redisService.deleteKey(token);
    }
}
