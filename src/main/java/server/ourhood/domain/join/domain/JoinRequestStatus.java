package server.ourhood.domain.join.domain;

public enum JoinRequestStatus {
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
