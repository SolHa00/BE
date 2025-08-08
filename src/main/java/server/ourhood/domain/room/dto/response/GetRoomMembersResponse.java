package server.ourhood.domain.room.dto.response;

import java.util.List;

import server.ourhood.domain.user.domain.User;

public record GetRoomMembersResponse(
	List<MemberInfo> members
) {
	public record MemberInfo(
		Long userId,
		String nickname
	) {
		public static MemberInfo from(User user) {
			return new MemberInfo(
				user.getId(),
				user.getNickname()
			);
		}
	}
}
