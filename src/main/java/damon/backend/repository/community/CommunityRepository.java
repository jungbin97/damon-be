package damon.backend.repository.community;

import damon.backend.entity.Community;
import damon.backend.enums.CommunityType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommunityRepository extends JpaRepository<Community, Long>  {

    @EntityGraph(attributePaths = {"member", "comments", "likes"})
    @Query("SELECT c FROM Community c")
    List<Community> findAllFetch();

    @EntityGraph(attributePaths = {"member", "comments", "likes"})
    @Query("SELECT c FROM Community c WHERE c.communityId = :communityId")
    Optional<Community> findOneFetch(@Param("communityId") Long communityId);

    @EntityGraph(attributePaths = {"member", "comments", "likes"})
    @Query("SELECT c FROM Community c WHERE c.type = :communityType")
    Page<Community> findAllFetchPaging(@Param("communityType") CommunityType communityType, Pageable pageable);
}
