package damon.backend.controller;


import damon.backend.dto.request.ReviewRequest;
import damon.backend.dto.response.ReviewListResponse;
import damon.backend.dto.response.ReviewResponse;
import damon.backend.entity.Area;
import damon.backend.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ReviewController {
    private final ReviewService reviewService;

    //게시글 등록
    @PostMapping
    public ReviewResponse postReview(
            @RequestBody ReviewRequest reviewRequest
            ){
        return reviewService.postReview(reviewRequest);
    }


    //게시글 목록 조회
    @GetMapping("/list")
    public List<ReviewListResponse> searchReviewList(
            @RequestParam("page") int page,
            @RequestParam("pageSize") int pageSize,
            @RequestParam(value = "area", required = false) Optional<Area> area
    ){
        return reviewService.searchReviewList(page, pageSize, area.orElse(null));
    }


    //게시글 상세 조회 (댓글 포함)
    @GetMapping("/{reviewId}")
    public ReviewResponse searchReviewDetail(@PathVariable Long reviewId) {
        ReviewResponse reviewResponse = reviewService.searchReview(reviewId);
        reviewService.incrementReviewViewCount(reviewId); // 조회수 증가
        return reviewResponse;
    }

    //게시글 수정
    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewResponse> updateReview(
            @PathVariable Long reviewId,
            @RequestBody ReviewRequest reviewRequest){
        ReviewResponse updatedReview = reviewService.updateReview(reviewId, reviewRequest); //memberId 추후에 추가
        return ResponseEntity.ok(updatedReview);
    }

    //게시글 삭제
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId); // memberId 추가
        return ResponseEntity.ok().build(); // HTTP 200 OK 응답
    }

    // 좋아요 토글
    @PatchMapping("/{reviewId}")
    public ResponseEntity<Void> toggleLike(@PathVariable Long reviewId) {
        reviewService.toggleLike(reviewId);
        return ResponseEntity.ok().build(); // HTTP 200 OK 응답
    }

    // freeTag 기반 검색
    @GetMapping("/list/search")
    public List<ReviewListResponse> searchReviewsByFreeTag(
            @RequestParam("freeTag") String freeTag,
            @RequestParam("page") int page,
            @RequestParam("pageSize") int pageSize
    ) {
        return reviewService.searchReviewsByFreeTag(freeTag, page, pageSize);
    }



}