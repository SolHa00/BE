package server.ourhood.domain.invitation.dao;

import static server.ourhood.domain.invitation.domain.QInvitation.*;
import static server.ourhood.domain.room.domain.QRoom.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import server.ourhood.domain.invitation.domain.Invitation;
import server.ourhood.domain.invitation.domain.InvitationStatus;
import server.ourhood.domain.room.domain.Room;
import server.ourhood.domain.user.domain.User;

@Repository
@RequiredArgsConstructor
public class InvitationRepositoryImpl implements InvitationRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<Invitation> findAllByInviteeAndStatusWithRoomAndHost(User user, InvitationStatus status) {
		return queryFactory
			.selectFrom(invitation)
			.join(invitation.room, room).fetchJoin()
			.join(room.host).fetchJoin()
			.where(inviteeEq(user), statusEq(status))
			.fetch();
	}

	@Override
	public List<Invitation> findAllByRoomAndStatusWithInvitee(Room room, InvitationStatus status) {
		return queryFactory
			.selectFrom(invitation)
			.join(invitation.invitee).fetchJoin()
			.where(roomEq(room), statusEq(status))
			.fetch();
	}

	private BooleanExpression inviteeEq(User user) {
		return user != null ? invitation.invitee.eq(user) : null;
	}

	private BooleanExpression roomEq(Room room) {
		return room != null ? invitation.room.eq(room) : null;
	}

	private BooleanExpression statusEq(InvitationStatus status) {
		return status != null ? invitation.status.eq(status) : null;
	}
}
