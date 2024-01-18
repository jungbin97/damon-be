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





}
