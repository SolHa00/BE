package server.ourhood.global.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BaseResponseStatus {

	/**
	 * 200 : 요청 성공
	 */
	SUCCESS(200, HttpStatus.OK, "요청 성공"),

	/**
	 * 400 BAD_REQUEST 잘못된 요청
	 */
	BAD_REQUEST(400, HttpStatus.BAD_REQUEST, "잘못된 요청"),
	KAKAO_INVALID_AUTH_CODE(40001, HttpStatus.BAD_REQUEST, "카카오 인증 코드가 유효하지 않습니다."),

	/**
	 * 401 UNAUTHORIZED 권한없음(인증 실패)
	 */
	UNAUTHORIZED(401, HttpStatus.UNAUTHORIZED, "인증 실패"),
	JWT_UNAUTHORIZED(40101, HttpStatus.UNAUTHORIZED, "JWT를 찾을 수 없습니다."),
	INVALID_ACCESS_TOKEN(40102, HttpStatus.UNAUTHORIZED, "access token이 유효하지 않습니다."),
	EXPIRED_REFRESH_TOKEN(40103, HttpStatus.UNAUTHORIZED, "refresh token이 만료되었습니다."),
	COOKIE_NOT_EXIST(40104, HttpStatus.UNAUTHORIZED, "쿠키가 존재하지 않습니다."),
	INVALID_COOKIE(40105, HttpStatus.UNAUTHORIZED, "유효하지 않은 쿠키입니다."),

	/**
	 * 403 FORBIDDEN 권한없음
	 */
	FORBIDDEN(403, HttpStatus.FORBIDDEN, "접근 권한이 없음"),

	/**
	 * 404 NOT_FOUND 잘못된 리소스 접근
	 */
	NOT_FOUND(404, HttpStatus.NOT_FOUND, "Not Found"),
	OAUTH_TYPE_NOT_FOUND(40401, HttpStatus.NOT_FOUND, "OAuth 타입을 찾을 수 없습니다."),

	/**
	 * 409 CONFLICT 중복된 리소스
	 */
	CONFLICT(409, HttpStatus.CONFLICT, "중복된 리소스"),

	/**
	 * 5XX Error
	 */
	INTERNAL_SERVER_ERROR(500, HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러"),
	KAKAO_SERVER_ERROR(50001, HttpStatus.INTERNAL_SERVER_ERROR, "카카오 로그인 서버와의 연결 과정에서 문제가 발생하였습니다."),
	KAKAO_INVALID_APP_INFO(50002, HttpStatus.INTERNAL_SERVER_ERROR, "카카오 애플리케이션 정보가 유효하지 않습니다."),
	KAKAO_INVALID_ACCESS_TOKEN(50003, HttpStatus.INTERNAL_SERVER_ERROR, "카카오 access token이 유효하지 않습니다.");

	private final int code;
	private final HttpStatus httpStatus;
	private final String message;
}
