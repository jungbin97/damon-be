package damon.backend.repository.community;

import damon.backend.entity.Community;
import damon.backend.entity.CommunityLike;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommunityLikeRepository extends JpaRepository<CommunityLike, Long> {

    @EntityGraph(attributePaths = {"community", "member"})
    @Query("SELECT l FROM CommunityLike l WHERE l.community.communityId = :communityId")
    List<CommunityLike> findAllFetch(@Param("communityId") Long communityId);

    @EntityGraph(attributePaths = {"community", "member"})
    @Query("SELECT l FROM CommunityLike l WHERE l.community.communityId = :communityId AND l.member.id = :memberId")
    Optional<CommunityLike> findOneFetch(@Param("communityId") Long communityId, @Param("memberId") Long memberId);

    boolean existsByCommunityCommunityIdAndMemberId(Long communityId, Long memberId);
}
