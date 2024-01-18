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
    public ResponseEntity<Void> updateComment(
            @PathVariable Long reviewId,
            @PathVariable Long commentId,
            @RequestBody ReviewCommentRequest request) {

        if (request.getContent() != null && !request.getContent().trim().isEmpty()) {
            reviewCommentService.updateComment(commentId, request);
        }

        // 대댓글의 경우 parentId를 사용하여 추가 로직 처리
        if (request.getParentId() != null) {
            // parentId를 사용한 로직
        }

        return ResponseEntity.ok().build();
    }

    // 댓글 삭제
    @DeleteMapping("/{reviewId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) { //@RequestParam Long memberId 추후에 추가

        reviewCommentService.deleteComment(commentId);
        return ResponseEntity.ok().build(); // HTTP 200 OK 응답
    }






}
