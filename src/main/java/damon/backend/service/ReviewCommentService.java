package damon.backend.service;

import damon.backend.dto.request.ReviewCommentRequest;
import damon.backend.dto.response.ReviewCommentResponse;
import damon.backend.dto.response.ReviewResponse;
import damon.backend.entity.Review;
import damon.backend.entity.ReviewComment;
import damon.backend.repository.ReviewCommentRepository;
import damon.backend.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewCommentService {
    private final ReviewRepository reviewRepository;
    private final ReviewCommentRepository reviewCommentRepository;
    private final ReviewService reviewService;


    // 댓글 등록
    public ReviewResponse postComment(Long reviewId, ReviewCommentRequest request) {

        // 리뷰 조회
        Review review = reviewRepository.findReviewWithCommentsAndRepliesByReviewId(reviewId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 리뷰입니다"));

        // 새로운 댓글 생성
        ReviewComment reviewComment = new ReviewComment();
        reviewComment.setContent(request.getContent());

        // 부모 댓글이 있을 경우
        if (request.getParentId() != null) {
            ReviewComment parent = reviewCommentRepository.findById(request.getParentId())
                    .orElseThrow(() -> new RuntimeException("존재하지 않는 댓글입니다"));

            // 대대댓글 생성 금지 로직
            if (parent.getParent() != null) {
                throw new RuntimeException("대대댓글은 허용되지 않습니다");
            }

            // 부모 댓글이 현재 리뷰에 속하는지 확인
            if (parent.getReview() == null || !parent.getReview().getId().equals(reviewId)) {
                throw new RuntimeException("부모 댓글이 현재 리뷰에 속하지 않습니다");
            }

            reviewComment.setParent(parent); // 리뷰+댓글 연결
            parent.addReply(reviewComment); // 부모 댓글에 대댓글 추가
        }

        // 댓글 존재 여부와 상관없이 리뷰 + 댓글 연결은 always
        reviewComment.setReview(review);

        // 댓글 저장
        reviewCommentRepository.save(reviewComment);

        // ReviewService의 메소드를 호출하여 댓글 구조를 조직화
        List<ReviewCommentResponse> organizedComments = reviewService.organizeCommentStructure(reviewId);

        // 구조화된 댓글 목록을 포함하여 ReviewResponse 반환
        return ReviewResponse.from(review, organizedComments);
    }






}
