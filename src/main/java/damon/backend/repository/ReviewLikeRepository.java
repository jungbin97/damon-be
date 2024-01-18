package damon.backend.repository;

import damon.backend.entity.ReviewLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {

    //좋아요 수 세기
    Long countByReviewId(Long reviewId);

    //특정 리뷰의 좋아요 추가 or 취소
    Optional<ReviewLike> findByReviewId(Long reviewId);


}
