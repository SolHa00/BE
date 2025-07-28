package server.ourhood.domain.moment.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import server.ourhood.domain.moment.domain.Moment;
import server.ourhood.domain.room.domain.Room;

@Repository
public interface MomentRepository extends JpaRepository<Moment, Long> {
	@Query("SELECT m FROM Moment m LEFT JOIN FETCH m.image WHERE m.room = :room")
	List<Moment> findAllByRoomWithImage(@Param("room") Room room);

	@Query("SELECT m FROM Moment m "
		+ "JOIN FETCH m.owner "
		+ "JOIN FETCH m.image "
		+ "WHERE m.id = :momentId")
	Optional<Moment> findByIdWithDetails(@Param("momentId") Long momentId);
}
