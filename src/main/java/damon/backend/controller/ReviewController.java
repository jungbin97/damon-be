package damon.backend.controller;

import damon.backend.dto.request.ReviewRequest;
import damon.backend.dto.response.ReviewListResponse;
import damon.backend.dto.response.ReviewResponse;
import damon.backend.entity.Area;
import damon.backend.service.ReviewService;
import damon.backend.util.Log;
import damon.backend.util.login.AuthToken;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Tag(name = "리뷰 API", description = "리뷰 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/review")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ReviewController {
    private final ReviewService reviewService;

    //게시글 등록
//    @PostMapping
//    public ReviewResponse addReview(
//            @RequestBody ReviewAndImageRequest form,
//            @Parameter(description = "유저 식별자", required = true, hidden = true)
//            @AuthToken String identifier
//    ) {
//        // 이미지 데이터 처리
//        Log.info(form);
//        Log.info(identifier);
//
//        return reviewService.addReview(form, identifier);
//    }

    //게시글 등록
    @PostMapping
    @Operation(summary = "내 리뷰 등록", description = "내 리뷰를 등록합니다.")
    @ApiResponse(responseCode = "200", description = "리뷰 등록 성공")
    public ReviewResponse postReview(
            @Valid
            @RequestBody ReviewRequest reviewRequest,
//            @RequestParam("images") Optional<List<MultipartFile>> images,
            @Parameter(description = "유저 식별자", required = true, hidden = true)
            @AuthToken String identifier
    ){
        return reviewService.postReview(reviewRequest, identifier);
    }


    //게시글 목록 조회
    @GetMapping("/list")
    @Operation(summary = "리뷰 전체 조회", description = "리뷰를 전체 조회합니다.")
    @ApiResponse(responseCode = "200", description = "리뷰 전체 조회 성공")
    public List<ReviewListResponse> searchReviewList(
            @Schema(description = "페이지 인덱스", example="0")
            @RequestParam("page") int page,
            @Schema(description = "한 페이지 당 보여질 리뷰 개수", example="10")
            @RequestParam("pageSize") int pageSize,

            @RequestParam(value = "area", required = false) Optional<Area> area
    ){
        return reviewService.searchReviewList(page, pageSize, area.orElse(null));
    }

    // 내 게시글 목록 조회
//    @GetMapping("/my/list")
//    @Operation(summary = "내 리뷰 전체 조회", description = "내 리뷰를 전체 조회합니다.")
//    @ApiResponse(responseCode = "200", description = "리뷰 전체 조회 성공")
//    public List<ReviewListResponse> searchMyReviewList(
//            @Parameter(description = "유저 식별자", required = true, hidden = true)
//            @AuthToken String identifier
//    ){
//        return reviewService.searchMyReviewList(identifier);
//    }


    //게시글 상세 조회 (댓글 포함)
    @GetMapping("/{reviewId}")
    @Operation(summary = "리뷰 상세 조회", description = "리뷰를 상세 조회합니다.")
    @ApiResponse(responseCode = "200", description = "리뷰 상세 조회 성공")
    public ReviewResponse searchReviewDetail(
            @Schema(description = "리뷰 인덱스", example="1")
            @PathVariable Long reviewId
    ) {
        ReviewResponse reviewResponse = reviewService.searchReview(reviewId);
        reviewService.incrementReviewViewCount(reviewId); // 조회수 증가
        return reviewResponse;
    }

    //게시글 수정
    @PutMapping("/{reviewId}")
    @Operation(summary = "내 리뷰 수정", description = "내 리뷰를 수정합니다.")
    @ApiResponse(responseCode = "200", description = "리뷰 수정 성공")
    public ResponseEntity<ReviewResponse> updateReview(
            @Schema(description = "리뷰 인덱스", example="1")
            @Valid
            @PathVariable Long reviewId,
            @RequestParam("images") Optional<List<MultipartFile>> newImages,
            @RequestParam("deleteImages") Optional<List<Long>> deleteImageIds,
            @RequestBody ReviewRequest reviewRequest,
            @Parameter(description = "유저 식별자", required = true, hidden = true)
            @AuthToken String identifier){
        ReviewResponse updatedReview = reviewService.updateReview(
                reviewId,
                reviewRequest,
                newImages.orElse(new ArrayList<>()), deleteImageIds.orElse(new ArrayList<>()),
                identifier
        ); //memberId 추후에 추가
        return ResponseEntity.ok(updatedReview);
    }

    //게시글 삭제
    @DeleteMapping("/{reviewId}")
    @Operation(summary = "내 리뷰 삭제", description = "내 리뷰를 삭제합니다.")
    @ApiResponse(responseCode = "200", description = "리뷰 등록 삭제")
    public ResponseEntity<Void> deleteReview(
            @Schema(description = "리뷰 인덱스", example="1")
            @PathVariable Long reviewId,
            @Parameter(description = "유저 식별자", required = true, hidden = true)
            @AuthToken String identifier
    ) {
        reviewService.deleteReview(reviewId, identifier); // memberId 추가
        return ResponseEntity.ok().build(); // HTTP 200 OK 응답
    }

    // 좋아요 토글
    @PatchMapping("/{reviewId}")
    @Operation(summary = "리뷰 좋아요 누르기", description = "리뷰에 좋아요를 누릅니다.")
    @ApiResponse(responseCode = "200", description = "리뷰 좋아요 성공")
    public ResponseEntity<ReviewResponse> toggleLike(
            @Schema(description = "리뷰 인덱스", example="1")
            @PathVariable Long reviewId,
            @Parameter(description = "유저 식별자", required = true, hidden = true)
            @AuthToken String identifier
    ) {
        reviewService.toggleLike(reviewId, identifier);
        return ResponseEntity.ok().build(); // 토글 후 리뷰의 최신 상태 반환 // HTTP 200 OK 응답
    }

    //좋아요 누른 게시글 목록
    @GetMapping("/likes")
    @Operation(summary = "내 좋아요 게시글", description = "좋아요 누른 게시글을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "리뷰 좋아요 조회 성공")
    public ResponseEntity<List<ReviewListResponse>> getLikedReviews(
            @Parameter(description = "유저 식별자", required = true, hidden = true)
            @AuthToken String identifier,
            @Schema(description = "페이지 인덱스", example="0")
            @RequestParam("page") int page,
            @Schema(description = "한 페이지 당 보여질 리뷰 개수", example="10")
            @RequestParam("pageSize") int pageSize
    ) {
        List<ReviewListResponse> likedReviews = reviewService.findLikedReviewsByUser(identifier, page, pageSize);
        return ResponseEntity.ok(likedReviews);
    }


    // 리뷰 검색
    @GetMapping("/list/search")
    @Operation(summary = "리뷰 검색", description = "작성자 이름, 리뷰 제목, 태그를 기반으로 리뷰를 검색합니다.")
    @ApiResponse(responseCode = "200", description = "리뷰 검색 성공")
    public List<ReviewListResponse> searchReviews(
            @Schema(description = "검색 옵션", example="작성자 / 제목 / 태그")
            @RequestParam("mode") String mode,
            @Schema(description = "검색어", example="강민우")
            @RequestParam("keyword") String keyword,
            @Schema(description = "페이지 인덱스", example="0")
            @RequestParam("page") int page,
            @Schema(description = "한 페이지 당 보여질 리뷰 개수", example="10")
            @RequestParam("pageSize") int pageSize) {
        return reviewService.searchReviews(mode, keyword, page, pageSize);
    }

    // 메인 페이지용 베스트 리뷰 조회
    @GetMapping("/best")
    @Operation(summary = "베스트 리뷰", description = "메인 페이지에서 베스트 리뷰를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "베스트 리뷰 조회 성공")

    public ResponseEntity<List<ReviewListResponse>> getTopReviewsForMainPage() {
        List<ReviewListResponse> topReviews = reviewService.findTopReviewsForMainPage(5);
        return ResponseEntity.ok(topReviews);
    }
}