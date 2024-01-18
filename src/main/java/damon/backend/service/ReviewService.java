package damon.backend.service;

import damon.backend.dto.request.ReviewRequest;
import damon.backend.dto.response.ReviewCommentResponse;
import damon.backend.dto.response.ReviewListResponse;
import damon.backend.dto.response.ReviewResponse;
import damon.backend.entity.Area;
import damon.backend.entity.Review;
import damon.backend.entity.ReviewComment;
import damon.backend.entity.ReviewLike;
import damon.backend.repository.ReviewCommentRepository;
import damon.backend.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewCommentRepository reviewCommentRepository;

    //게시글 등록
    public ReviewResponse postReview(ReviewRequest request) {
        Review review = new Review();
        review.setTitle(request.getTitle());
        review.setStartDate(request.getStartDate());
        review.setEndDate(request.getEndDate());
        review.setArea(request.getArea());
        review.setCost(request.getCost());
        review.setSuggests(request.getSuggests());
        review.setFreeTags(request.getFreeTags());
        review.setContent(request.getContent());
        review = reviewRepository.save(review); // 리뷰 저장

        List<ReviewCommentResponse> emptyCommentsList = new ArrayList<>(); // 새 리뷰에는 댓글이 없으므로 빈 리스트 생성
        return ReviewResponse.from(review, emptyCommentsList); // 저장된 리뷰와 빈 댓글 목록을 전달
    }

    // 게시글 전체 조회
    @Transactional(readOnly = true)
    public List<ReviewListResponse> searchReviewList(int page, int pageSize, Area area) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "createTime"));
        Page<Review> reviewPage;

        if (area != null) {
            reviewPage = reviewRepository.findByAreaWithSuggests(area, pageable);
        } else {
            reviewPage = reviewRepository.findAll(pageable);
        }
        if (reviewPage.hasContent()) {
            return reviewPage.map(ReviewListResponse::from).toList();
        } else {
            return new ArrayList<>();
        }
    }



    //게시글 상세 내용 조회 (댓글 포함)
    @Transactional(readOnly = true)
    public ReviewResponse searchReview(Long reviewId) {
        Review review = reviewRepository.findReviewWithCommentsAndRepliesByReviewId(reviewId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 리뷰입니다"));

        // 댓글 계층 구조 생성
        List<ReviewCommentResponse> organizedComments = organizeCommentStructure(reviewId);

        // DTO 반환
        return ReviewResponse.from(review, organizedComments); // 이 부분에서 organizedComments를 DTO에 포함시켜야 합니다.
    }



    // 댓글, 대댓글 계층적 구조 생성
    public List<ReviewCommentResponse> organizeCommentStructure(Long reviewId) {
        List<ReviewComment> allComments = reviewCommentRepository.findByReviewIdOrderByCreateTimeAsc(reviewId);
        Map<Long, ReviewCommentResponse> commentResponseMap = new HashMap<>();
        List<ReviewCommentResponse> topLevelComments = new ArrayList<>();

        // 모든 댓글을 Response 객체로 변환하고, Map에 저장 (ID를 키로 사용)
        for (ReviewComment comment : allComments) {
            ReviewCommentResponse commentResponse = ReviewCommentResponse.from(comment);
            commentResponseMap.put(comment.getId(), commentResponse);
            if (comment.getParent() == null) { // 부모 댓글일 경우
                topLevelComments.add(commentResponse);
            }
        }

        // 대댓글을 부모 댓글의 replies 목록에 추가
        for (ReviewComment comment : allComments) {
            if (comment.getParent() != null) { // 대댓글일 경우
                ReviewCommentResponse childCommentResponse = commentResponseMap.get(comment.getId());
                ReviewCommentResponse parentCommentResponse = commentResponseMap.get(comment.getParent().getId());
                if (parentCommentResponse != null && !parentCommentResponse.getReplies().contains(childCommentResponse)) {
                    parentCommentResponse.getReplies().add(childCommentResponse);
                }
            }
        }

        return topLevelComments;
    }


}