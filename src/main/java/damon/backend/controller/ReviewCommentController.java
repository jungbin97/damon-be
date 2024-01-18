package damon.backend.controller;

import damon.backend.dto.request.ReviewCommentRequest;
import damon.backend.dto.response.ReviewResponse;
import damon.backend.service.ReviewCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ReviewCommentController {
    private final ReviewCommentService reviewCommentService;

    // 댓글 등록
    @PostMapping("/{reviewId}/comments")
    public ReviewResponse postComment(
            @PathVariable Long reviewId,
            @RequestBody ReviewCommentRequest reviewCommentRequest) {
        return reviewCommentService.postComment(reviewId, reviewCommentRequest);
    }


    // 댓글 수정
    @PatchMapping("/{reviewId}/comments/{commentId}")
    public ResponseEntity<ReviewResponse> updateComment(
            @PathVariable Long reviewId,
            @PathVariable Long commentId,
            @RequestBody ReviewCommentRequest request) {

        if (request.getContent() != null && !request.getContent().trim().isEmpty()) {
            ReviewResponse updatedReview = reviewCommentService.updateComment(commentId, request);
            return ResponseEntity.ok(updatedReview); // 수정된 리뷰의 최신 상태를 반환
        }
        return ResponseEntity.badRequest().build();

       }

    // 댓글 삭제
    @DeleteMapping("/{reviewId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) { //@RequestParam Long memberId 추후에 추가

        reviewCommentService.deleteComment(commentId);
        return ResponseEntity.ok().build(); // HTTP 200 OK 응답
    }



}
