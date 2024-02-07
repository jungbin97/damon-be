package damon.backend.repository.community;

import damon.backend.entity.Community;
import damon.backend.entity.CommunityComment;
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
public interface CommunityCommentRepository extends JpaRepository<CommunityComment, Long> {

    @EntityGraph(attributePaths = {"community", "member", "parentComment"})
    @Query("SELECT c FROM CommunityComment c WHERE c.community.communityId = :communityId")
    List<CommunityComment> findAllFetch(@Param("communityId") Long communityId);

    @EntityGraph(attributePaths = {"community", "member", "parentComment"})
    @Query("SELECT c FROM CommunityComment c WHERE c.commentId = :commentId")
    Optional<CommunityComment> findOneFetch(@Param("commentId") Long commentId);
}
