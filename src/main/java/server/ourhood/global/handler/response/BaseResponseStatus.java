package server.ourhood.global.handler.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

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

    /**
     * 401 UNAUTHORIZED 권한없음(인증 실패)
     */
    UNAUTHORIZED(401, HttpStatus.UNAUTHORIZED, "인증 실패"),

    /**
     * 403 FORBIDDEN 권한없음
     */
    FORBIDDEN(403, HttpStatus.FORBIDDEN, "접근 권한이 없음"),

    /**
     * 404 NOT_FOUND 잘못된 리소스 접근
     */
    NOT_FOUND( 404, HttpStatus.NOT_FOUND,"Not Found"),

    /**
     * 409 CONFLICT 중복된 리소스
     */
    CONFLICT(409, HttpStatus.CONFLICT, "중복된 리소스"),

    /**
     * 5XX Error
     */
    INTERNAL_SERVER_ERROR( 500, HttpStatus.INTERNAL_SERVER_ERROR,"서버 에러");

    private final int code;
    private final HttpStatus httpStatus;
    private final String message;
}
