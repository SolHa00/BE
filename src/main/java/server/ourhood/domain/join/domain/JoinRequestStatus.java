package server.ourhood.domain.join.domain;

public enum JoinRequestStatus {
	PENDING,  // 대기
	ACCEPTED, // 수락
	REJECTED;  // 거절

	public static JoinRequestStatus fromName(String status) {
		return JoinRequestStatus.valueOf(status.toUpperCase());
	}
}
