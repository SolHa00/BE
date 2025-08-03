package server.ourhood.domain.room.dao;

import static server.ourhood.domain.room.domain.QRoom.*;
import static server.ourhood.domain.room.domain.QRoomMembers.*;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import server.ourhood.domain.room.domain.Room;
import server.ourhood.domain.user.domain.User;

@Repository
@RequiredArgsConstructor
public class RoomRepositoryImpl implements RoomRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<Room> findAllByMemberWithDetails(User user) {
		return queryFactory
			.selectDistinct(room)
			.from(room)
			.join(room.roomMembers, roomMembers).fetchJoin()
			.join(room.host).fetchJoin()
			.leftJoin(room.thumbnailImage).fetchJoin()
			.where(roomMembers.user.eq(user))
			.fetch();
	}

	@Override
	public Optional<Room> findByIdWithAllDetails(Long roomId) {
		return Optional.ofNullable(queryFactory
			.selectFrom(room)
			.join(room.roomMembers, roomMembers).fetchJoin()
			.join(room.host).fetchJoin()
			.leftJoin(room.thumbnailImage).fetchJoin()
			.join(roomMembers.user).fetchJoin()
			.where(room.id.eq(roomId))
			.fetchOne());
	}

	@Override
	public Optional<Room> findByIdWithHostAndThumbnail(Long roomId) {
		return Optional.ofNullable(queryFactory
			.selectFrom(room)
			.join(room.host).fetchJoin()
			.leftJoin(room.thumbnailImage).fetchJoin()
			.where(room.id.eq(roomId))
			.fetchOne());
	}
}
