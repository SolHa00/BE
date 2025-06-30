package server.ourhood.domain.moment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import server.ourhood.domain.moment.domain.Moment;

@Repository
public interface MomentRepository extends JpaRepository<Moment, Long> {
}
