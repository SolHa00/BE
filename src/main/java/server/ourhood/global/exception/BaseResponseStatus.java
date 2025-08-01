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
	INVALID_IMAGE_FILE_EXTENSION(40002, HttpStatus.BAD_REQUEST, "올바른 이미지 확장자가 아닙니다."),
	HOST_CANNOT_LEAVE_ROOM(40003, HttpStatus.BAD_REQUEST, "방장은 방을 나갈 수 없습니다."),
	INVALID_COMMENT_LEVEL(40004, HttpStatus.BAD_REQUEST, "대댓글에는 댓글을 달 수 없습니다."),

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
	NOT_ROOM_HOST(40302, HttpStatus.FORBIDDEN, "Room Host만 해당 작업을 수행할 수 있습니다."),
	NOT_MOMENT_OWNER(40303, HttpStatus.FORBIDDEN, "Moment 소유자만 해당 작업을 수행할 수 있습니다."),
	NOT_COMMENT_OWNER(40304, HttpStatus.FORBIDDEN, "Comment 소유자만 해당 작업을 수행할 수 있습니다."),
	NOT_ROOM_MEMBER(40305, HttpStatus.FORBIDDEN, "Room 멤버만 해당 작업을 수행할 수 있습니다."),
	NOT_ROOM_REQUESTER(40306, HttpStatus.FORBIDDEN, "참여 요청을 보낸 사용자만 해당 작업을 수행할 수 있습니다."),
	NOT_ROOM_INVITER(40307, HttpStatus.FORBIDDEN, "초대 받은 사용자만 해당 작업을 수행할 수 있습니다."),

	/**
	 * 404 NOT_FOUND 잘못된 리소스 접근
	 */
	NOT_FOUND(404, HttpStatus.NOT_FOUND, "Not Found"),
	NOT_FOUND_AUTH_TYPE(40401, HttpStatus.NOT_FOUND, "OAuth 타입을 찾을 수 없습니다."),
	NOT_FOUND_USER(40402, HttpStatus.NOT_FOUND, "User를 찾을 수 없습니다."),
	NOT_FOUND_ROOM(40403, HttpStatus.NOT_FOUND, "Room을 찾을 수 없습니다."),
	USER_NOT_IN_ROOM(40404, HttpStatus.NOT_FOUND, "사용자가 방에 속해있지 않습니다"),
	NOT_FOUND_MOMENT(40405, HttpStatus.NOT_FOUND, "존재하지 않는 Moment입니다."),
	NOT_FOUND_COMMENT(40406, HttpStatus.NOT_FOUND, "존재하지 않는 Comment입니다."),
	NOT_FOUND_JOIN_REQUEST(40407, HttpStatus.NOT_FOUND, "존재하지 않는 Join Request입니다."),
	NOT_FOUND_INVITATION(40408, HttpStatus.NOT_FOUND, "존재하지 않는 Invitation입니다."),
	IMAGE_FILE_EXTENSION_NOT_FOUND(40409, HttpStatus.NOT_FOUND, "이미지 파일 확장자를 찾을 수 없습니다."),
	NOT_FOUND_IMAGE(40410, HttpStatus.NOT_FOUND, "이미지를 찾을 수 없습니다."),
	NOT_FOUND_IMAGE_FILE_IN_S3(40411, HttpStatus.NOT_FOUND, "해당 파일은 S3내에 존재하지 않습니다."),

	/**
	 * 409 CONFLICT 중복된 리소스
	 */
	CONFLICT(409, HttpStatus.CONFLICT, "중복된 리소스"),
	CONFLICT_JOIN_REQUEST(40901, HttpStatus.CONFLICT, "이미 해당 방에 참여 요청을 보냈습니다."),
	CONFLICT_INVITATION(40902, HttpStatus.CONFLICT, "이미 해당 사용자에게 초대 요청을 보냈습니다."),
	ALREADY_PROCESSED_INVITATION(40903, HttpStatus.CONFLICT, "이미 처리된 초대 요청입니다."),
	ALREADY_PROCESSED_JOIN_REQUEST(40904, HttpStatus.CONFLICT, "이미 처리된 참여 요청입니다."),
	ALREADY_MEMBER_IN_ROOM(40905, HttpStatus.CONFLICT, "이미 해당 방에 속해있는 사용자입니다."),

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
