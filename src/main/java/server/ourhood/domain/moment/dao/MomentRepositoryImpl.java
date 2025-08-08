package server.ourhood.domain.moment.dao;

import static server.ourhood.domain.moment.domain.QMoment.*;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import server.ourhood.domain.moment.domain.Moment;
import server.ourhood.domain.room.domain.Room;

@Repository
@RequiredArgsConstructor
public class MomentRepositoryImpl implements MomentRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<Moment> findAllByRoomWithImageOrderByCreatedAtDesc(Room room) {
		return queryFactory
			.selectFrom(moment)
			.leftJoin(moment.image).fetchJoin()
			.where(moment.room.eq(room))
			.orderBy(moment.createdAt.desc())
			.fetch();
	}

	@Override
	public Optional<Moment> findByIdWithOwnerAndImage(Long momentId) {
		return Optional.ofNullable(
			queryFactory
				.selectFrom(moment)
				.join(moment.owner).fetchJoin()
				.leftJoin(moment.image).fetchJoin()
				.where(moment.id.eq(momentId))
				.fetchOne());
	}
}
