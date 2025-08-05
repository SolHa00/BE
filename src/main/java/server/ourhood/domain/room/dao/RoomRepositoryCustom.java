package server.ourhood.domain.room.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;

import server.ourhood.domain.room.domain.Room;
import server.ourhood.domain.room.dto.request.RoomSearchCondition;
import server.ourhood.domain.user.domain.User;

public interface RoomRepositoryCustom {
	List<Room> findAllByMemberWithHostAndThumbnailAndMembers(User user);

	Optional<Room> findByIdWithHostAndThumbnailAndMembersWithUser(Long roomId);

	Optional<Room> findByIdWithHostAndThumbnail(Long roomId);

	List<Room> searchRooms(RoomSearchCondition condition, String keyword, Sort sort);
}
