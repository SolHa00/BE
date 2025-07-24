package server.ourhood.domain.room.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import server.ourhood.domain.room.domain.Room;
import server.ourhood.domain.user.domain.User;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
	@Query("SELECT r FROM Room r LEFT JOIN FETCH r.thumbnailImage WHERE r IN (SELECT rm.room FROM RoomMembers rm WHERE rm.user = :user)")
	List<Room> findAllByMemberWithThumbnail(@Param("user") User user);
}
