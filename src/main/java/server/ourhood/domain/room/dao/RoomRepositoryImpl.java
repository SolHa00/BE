package server.ourhood.domain.room.dao;

import static server.ourhood.domain.room.domain.QRoom.*;
import static server.ourhood.domain.room.domain.QRoomMembers.*;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import server.ourhood.domain.room.domain.Room;
import server.ourhood.domain.room.dto.request.RoomSearchCondition;
import server.ourhood.domain.user.domain.User;

@Repository
@RequiredArgsConstructor
public class RoomRepositoryImpl implements RoomRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<Room> findAllByMemberWithHostAndThumbnailAndMembers(User user) {
		return queryFactory
			.selectDistinct(room)
			.from(room)
			.join(room.host).fetchJoin()
			.leftJoin(room.thumbnailImage).fetchJoin()
			.join(room.roomMembers, roomMembers).fetchJoin()
			.where(roomMembers.user.eq(user))
			.fetch();
	}

	@Override
	public Optional<Room> findByIdWithHostAndThumbnailAndMembersWithUser(Long roomId) {
		return Optional.ofNullable(
			queryFactory
				.selectFrom(room)
				.join(room.host).fetchJoin()
				.leftJoin(room.thumbnailImage).fetchJoin()
				.join(room.roomMembers, roomMembers).fetchJoin()
				.join(roomMembers.user).fetchJoin()
				.where(room.id.eq(roomId))
				.fetchOne());
	}

	@Override
	public Optional<Room> findByIdWithHostAndThumbnail(Long roomId) {
		return Optional.ofNullable(
			queryFactory
				.selectFrom(room)
				.join(room.host).fetchJoin()
				.leftJoin(room.thumbnailImage).fetchJoin()
				.where(room.id.eq(roomId))
				.fetchOne());
	}

	@Override
	public List<Room> searchRooms(RoomSearchCondition condition, String keyword, Sort sort) {
		return queryFactory
			.selectDistinct(room)
			.from(room)
			.join(room.host).fetchJoin()
			.join(room.roomMembers, roomMembers).fetchJoin()
			.leftJoin(room.thumbnailImage).fetchJoin()
			.where(createSearchCondition(condition, keyword))
			.orderBy(createOrderSpecifier(sort))
			.fetch();
	}

	private BooleanExpression createSearchCondition(RoomSearchCondition condition, String keyword) {
		if (!StringUtils.hasText(keyword) || condition == null) {
			return null;
		}

		return switch (condition) {
			case ROOM -> room.name.containsIgnoreCase(keyword);
			case HOST -> room.host.nickname.containsIgnoreCase(keyword);
		};
	}

	private OrderSpecifier<?>[] createOrderSpecifier(Sort sort) {
		return sort.stream()
			.map(order -> {
				Order direction = order.isAscending() ? Order.ASC : Order.DESC;
				String property = order.getProperty();
				return switch (property) {
					case "createdAt" -> new OrderSpecifier<>(direction, room.createdAt);
					default -> new OrderSpecifier<>(Order.DESC, room.createdAt);
				};
			})
			.toArray(OrderSpecifier[]::new);
	}
}
