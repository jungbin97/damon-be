package damon.backend.repository;

import damon.backend.entity.Review;
import damon.backend.entity.ReviewLike;
import damon.backend.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {


    //특정 리뷰의 좋아요 추가 or 취소
    Optional<ReviewLike> findByReviewAndUser(Review review, User user);

    //좋아요 누른 게시글
    Page<ReviewLike> findByUser(User user, Pageable pageable);



}
