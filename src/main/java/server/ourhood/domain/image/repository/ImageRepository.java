package server.ourhood.domain.image.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import server.ourhood.domain.image.domain.Image;

@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {
	Optional<Image> findByImageKey(String imageKey);
}
