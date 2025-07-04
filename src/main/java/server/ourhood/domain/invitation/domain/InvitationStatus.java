package server.ourhood.domain.invitation.domain;

public enum InvitationStatus {
	PENDING,  // 대기
	ACCEPTED, // 수락
	REJECTED;  // 거절

	public static InvitationStatus fromName(String status) {
		return InvitationStatus.valueOf(status.toUpperCase());
	}
}
