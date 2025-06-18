package server.ourhood.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import server.ourhood.domain.auth.domain.OAuthIdentifier;
import server.ourhood.domain.user.domain.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByOauthIdentifier(OAuthIdentifier oauthIdentifier);
}