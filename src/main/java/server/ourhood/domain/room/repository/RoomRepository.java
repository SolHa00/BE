package server.ourhood.domain.room.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import server.ourhood.domain.room.domain.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
}
