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
        return reviewService.searchReview(reviewId);
    }



}