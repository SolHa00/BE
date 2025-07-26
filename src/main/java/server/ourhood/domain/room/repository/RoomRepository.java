package server.ourhood.domain.room.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import server.ourhood.domain.room.domain.Room;
import server.ourhood.domain.user.domain.User;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
	@Query("SELECT DISTINCT r FROM Room r "
		+ "JOIN FETCH r.roomMembers rm "
		+ "JOIN FETCH r.host "
		+ "LEFT JOIN FETCH r.thumbnailImage "
		+ "WHERE rm.user = :user")
	List<Room> findAllByMemberWithDetails(@Param("user") User user);

	@Query("SELECT r FROM Room r "
		+ "JOIN FETCH r.host "
		+ "JOIN FETCH r.roomMembers rm "
		+ "JOIN FETCH rm.user "
		+ "LEFT JOIN FETCH r.thumbnailImage "
		+ "WHERE r.id = :roomId")
	Optional<Room> findByIdWithAllDetails(@Param("roomId") Long roomId);

	@Query("SELECT r FROM Room r "
		+ "JOIN FETCH r.host "
		+ "LEFT JOIN FETCH r.thumbnailImage "
		+ "WHERE r.id = :roomId")
	Optional<Room> findByIdWithHostAndThumbnail(@Param("roomId") Long roomId);

	boolean existsByIdAndRoomMembersUser(Long id, User user);
}
