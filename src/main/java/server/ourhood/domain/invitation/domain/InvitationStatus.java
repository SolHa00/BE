package server.ourhood.domain.invitation.domain;

public enum InvitationStatus {

	REQUESTED,
	ACCEPTED,
	REJECTED,
	CANCELED;

	public boolean isAccepted() {
		return this == ACCEPTED;
	}

	public boolean isRejected() {
		return this == REJECTED;
	}

	public boolean isCanceled() {
		return this == CANCELED;
	}
}
