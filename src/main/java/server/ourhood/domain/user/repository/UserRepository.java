package server.ourhood.domain.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import server.ourhood.domain.auth.domain.OAuthIdentifier;
import server.ourhood.domain.user.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByOauthIdentifier(OAuthIdentifier oauthIdentifier);
}