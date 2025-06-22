package server.ourhood.global.util;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import server.ourhood.global.exception.BaseException;
import server.ourhood.global.exception.BaseResponseStatus;

@Component
public class CookieUtil {

	@Value("${cookie.refresh-token.expiry}")
	private int refreshTokenExpiry;

	@Value("${cookie.refresh-token.name}")
	private String refreshTokenCookieName;

	// 쿠키 생성 및 추가
	public void addCookie(HttpServletResponse response, String value) {
		Cookie cookie = new Cookie(refreshTokenCookieName, value);
		cookie.setHttpOnly(true);
		cookie.setPath("/");
		cookie.setMaxAge(refreshTokenExpiry);
		response.addCookie(cookie);
	}

	// 쿠키 삭제
	public void deleteCookie(HttpServletResponse response) {
		Cookie cookie = new Cookie(refreshTokenCookieName, null);
		cookie.setHttpOnly(true);
		cookie.setPath("/");
		cookie.setMaxAge(0); // 즉시 만료
		response.addCookie(cookie);
	}

	// 쿠키 조회
	public String getRefreshToken(Cookie[] cookies) {
		if (cookies == null) {
			throw new BaseException(BaseResponseStatus.COOKIE_NOT_EXIST);
		}
		return Arrays.stream(cookies)
			.filter(cookie -> cookie.getName().equals(refreshTokenCookieName))
			.findFirst()
			.map(Cookie::getValue)
			.orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_COOKIE));
	}
}
