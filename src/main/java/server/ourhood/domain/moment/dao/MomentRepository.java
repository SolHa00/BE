package server.ourhood.domain.moment.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import server.ourhood.domain.moment.domain.Moment;

public interface MomentRepository extends JpaRepository<Moment, Long>, MomentRepositoryCustom {
}
