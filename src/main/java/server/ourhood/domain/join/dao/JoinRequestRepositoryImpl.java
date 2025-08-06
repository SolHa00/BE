package server.ourhood.domain.join.dao;

import static server.ourhood.domain.join.domain.QJoinRequest.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import server.ourhood.domain.join.domain.JoinRequest;
import server.ourhood.domain.join.domain.JoinRequestStatus;
import server.ourhood.domain.room.domain.Room;
import server.ourhood.domain.user.domain.User;

@Repository
@RequiredArgsConstructor
public class JoinRequestRepositoryImpl implements JoinRequestRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<JoinRequest> findAllByRequesterAndStatusWithRoom(User user, JoinRequestStatus status) {
		return queryFactory
			.selectFrom(joinRequest)
			.join(joinRequest.room).fetchJoin()
			.where(requesterEq(user), statusEq(status))
			.fetch();
	}

	@Override
	public List<JoinRequest> findAllByRoomAndStatusWithRequester(Room room, JoinRequestStatus status) {
		return queryFactory
			.selectFrom(joinRequest)
			.join(joinRequest.requester).fetchJoin()
			.where(roomEq(room), statusEq(status))
			.fetch();
	}

	private BooleanExpression requesterEq(User user) {
		return user != null ? joinRequest.requester.eq(user) : null;
	}

	private BooleanExpression roomEq(Room room) {
		return room != null ? joinRequest.room.eq(room) : null;
	}

	private BooleanExpression statusEq(JoinRequestStatus status) {
		return status != null ? joinRequest.status.eq(status) : null;
	}
}
