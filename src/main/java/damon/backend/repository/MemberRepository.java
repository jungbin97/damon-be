package damon.backend.repository;

import damon.backend.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByProvidername(String providername);

    Optional<Member> findByProvider(String provider);
}
