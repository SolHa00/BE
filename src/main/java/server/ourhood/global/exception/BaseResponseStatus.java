package server.ourhood.global.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BaseResponseStatus {

	/**
	 * 1000 : 요청 성공
	 */
	SUCCESS(true, 1000, HttpStatus.OK, "요청이 성공하였습니다."),

	/**
	 * 400 BAD_REQUEST 잘못된 요청
	 */
	BAD_REQUEST(false, 400, HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
	MISSING_PATH_VARIABLE(false, 40001, HttpStatus.BAD_REQUEST, "경로 변수가 누락되었습니다."),
	MISSING_REQUEST_PARAM(false, 40002, HttpStatus.BAD_REQUEST, "쿼리 파라미터가 누락되었습니다."),
	MISSING_REQUEST_PART(false, 40003, HttpStatus.BAD_REQUEST, "multipart/form-data 파일이 누락되었습니다."),
	REQ_BINDING_FAIL(false, 40004, HttpStatus.BAD_REQUEST, "잘못된 request 입니다."),
	FAILED_VALIDATION(false, 40005, HttpStatus.BAD_REQUEST, "입력값이 누락되었거나, 부적절한 입력 값이 있습니다."),
	MISMATCH_PARAM_TYPE(false, 40006, HttpStatus.BAD_REQUEST, "잘못된 파라미터 타입입니다."),
	KAKAO_INVALID_AUTH_CODE(false, 40007, HttpStatus.BAD_REQUEST, "카카오 인증 코드가 유효하지 않습니다."),
	INVALID_IMAGE_FILE_EXTENSION(false, 40008, HttpStatus.BAD_REQUEST, "올바른 이미지 확장자가 아닙니다."),
	HOST_CANNOT_LEAVE_ROOM(false, 40009, HttpStatus.BAD_REQUEST, "방장은 방을 나갈 수 없습니다."),
	INVALID_COMMENT_LEVEL(false, 40010, HttpStatus.BAD_REQUEST, "대댓글에는 댓글을 달 수 없습니다."),

	/**
	 * 401 UNAUTHORIZED 권한없음(인증 실패)
	 */
	UNAUTHORIZED(false, 401, HttpStatus.UNAUTHORIZED, "인증에 실패했습니다."),
	JWT_UNAUTHORIZED(false, 40101, HttpStatus.UNAUTHORIZED, "JWT를 찾을 수 없습니다."),
	INVALID_ACCESS_TOKEN(false, 40102, HttpStatus.UNAUTHORIZED, "access token이 유효하지 않습니다."),
	EXPIRED_REFRESH_TOKEN(false, 40103, HttpStatus.UNAUTHORIZED, "refresh token이 만료되었습니다."),
	COOKIE_NOT_EXIST(false, 40104, HttpStatus.UNAUTHORIZED, "쿠키가 존재하지 않습니다."),
	INVALID_COOKIE(false, 40105, HttpStatus.UNAUTHORIZED, "유효하지 않은 쿠키입니다."),

	/**
	 * 403 FORBIDDEN 권한없음
	 */
	FORBIDDEN(false, 403, HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),
	NOT_ROOM_HOST(false, 40302, HttpStatus.FORBIDDEN, "Room Host만 해당 작업을 수행할 수 있습니다."),
	NOT_MOMENT_OWNER(false, 40303, HttpStatus.FORBIDDEN, "Moment 소유자만 해당 작업을 수행할 수 있습니다."),
	NOT_COMMENT_OWNER(false, 40304, HttpStatus.FORBIDDEN, "Comment 소유자만 해당 작업을 수행할 수 있습니다."),
	NOT_ROOM_MEMBER(false, 40305, HttpStatus.FORBIDDEN, "Room 멤버만 해당 작업을 수행할 수 있습니다."),
	NOT_ROOM_REQUESTER(false, 40306, HttpStatus.FORBIDDEN, "참여 요청을 보낸 사용자만 해당 작업을 수행할 수 있습니다."),
	NOT_ROOM_INVITER(false, 40307, HttpStatus.FORBIDDEN, "초대 받은 사용자만 해당 작업을 수행할 수 있습니다."),

	/**
	 * 404 NOT_FOUND 잘못된 리소스 접근
	 */
	NOT_FOUND(false, 404, HttpStatus.NOT_FOUND, "Not Found"),
	NOT_FOUND_AUTH_TYPE(false, 40401, HttpStatus.NOT_FOUND, "OAuth 타입을 찾을 수 없습니다."),
	NOT_FOUND_USER(false, 40402, HttpStatus.NOT_FOUND, "User를 찾을 수 없습니다."),
	NOT_FOUND_ROOM(false, 40403, HttpStatus.NOT_FOUND, "Room을 찾을 수 없습니다."),
	USER_NOT_IN_ROOM(false, 40404, HttpStatus.NOT_FOUND, "사용자가 방에 속해있지 않습니다"),
	NOT_FOUND_MOMENT(false, 40405, HttpStatus.NOT_FOUND, "존재하지 않는 Moment입니다."),
	NOT_FOUND_COMMENT(false, 40406, HttpStatus.NOT_FOUND, "존재하지 않는 Comment입니다."),
	NOT_FOUND_JOIN_REQUEST(false, 40407, HttpStatus.NOT_FOUND, "존재하지 않는 Join Request입니다."),
	NOT_FOUND_INVITATION(false, 40408, HttpStatus.NOT_FOUND, "존재하지 않는 Invitation입니다."),
	IMAGE_FILE_EXTENSION_NOT_FOUND(false, 40409, HttpStatus.NOT_FOUND, "이미지 파일 확장자를 찾을 수 없습니다."),
	NOT_FOUND_IMAGE(false, 40410, HttpStatus.NOT_FOUND, "이미지를 찾을 수 없습니다."),
	NOT_FOUND_IMAGE_FILE_IN_S3(false, 40411, HttpStatus.NOT_FOUND, "해당 파일은 S3내에 존재하지 않습니다."),

	/**
	 * 405 METHOD_NOT_ALLOWED 지원하지 않은 method 호출
	 */
	METHOD_NOT_ALLOWED(false, 405, HttpStatus.METHOD_NOT_ALLOWED, "해당 method는 지원하지 않습니다."),

	/**
	 * 406 NOT_ACCEPTABLE 인식할 수 없는 content type
	 */
	NOT_ACCEPTABLE(false, 406, HttpStatus.NOT_ACCEPTABLE, "인식할 수 없는 미디어 타입입니다."),

	/**
	 * 409 CONFLICT 중복된 리소스
	 */
	CONFLICT(false, 409, HttpStatus.CONFLICT, "중복된 리소스입니다."),
	CONFLICT_JOIN_REQUEST(false, 40901, HttpStatus.CONFLICT, "이미 해당 방에 참여 요청을 보냈습니다."),
	CONFLICT_INVITATION(false, 40902, HttpStatus.CONFLICT, "이미 해당 사용자에게 초대 요청을 보냈습니다."),
	ALREADY_PROCESSED_INVITATION(false, 40903, HttpStatus.CONFLICT, "이미 처리된 초대 요청입니다."),
	ALREADY_PROCESSED_JOIN_REQUEST(false, 40904, HttpStatus.CONFLICT, "이미 처리된 참여 요청입니다."),
	ALREADY_MEMBER_IN_ROOM(false, 40905, HttpStatus.CONFLICT, "이미 해당 방에 속해있는 사용자입니다."),

	/**
	 * 415 UNSUPPORTED_MEDIA_TYPE 지원하지 않는 content type
	 */
	UNSUPPORTED_MEDIA_TYPE(false, 415, HttpStatus.UNSUPPORTED_MEDIA_TYPE, "지원하지 않는 미디어 타입입니다."),

	/**
	 * 500 INTERNAL_SERVER_ERROR 서버 내부 에러
	 */
	INTERNAL_SERVER_ERROR(false, 500, HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 에러입니다."),
	KAKAO_SERVER_ERROR(false, 50001, HttpStatus.INTERNAL_SERVER_ERROR, "카카오 로그인 서버와의 연결 과정에서 문제가 발생하였습니다."),
	KAKAO_INVALID_APP_INFO(false, 50002, HttpStatus.INTERNAL_SERVER_ERROR, "카카오 애플리케이션 정보가 유효하지 않습니다."),
	KAKAO_INVALID_ACCESS_TOKEN(false, 50003, HttpStatus.INTERNAL_SERVER_ERROR, "카카오 access token이 유효하지 않습니다."),

	/**
	 * 503 SERVICE_UNAVAILABLE 서버 내부 에러
	 */
	SERVICE_UNAVAILABLE(false, 503, HttpStatus.SERVICE_UNAVAILABLE, "현재 서비스가 불가능한 상태입니다."),
	INTERNAL_SERVER_TIME_OUT(false, 50301, HttpStatus.SERVICE_UNAVAILABLE, "서버에서 시간초과가 발생했습니다.");

	private final boolean isSuccess;
	private final int code;
	private final HttpStatus httpStatus;
	private final String message;
}
