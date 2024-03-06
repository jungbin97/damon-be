package damon.backend.service;
import damon.backend.dto.request.ReviewRequest;
import damon.backend.dto.response.ReviewCommentResponse;
import damon.backend.dto.response.ReviewListResponse;
import damon.backend.dto.response.ReviewResponse;
import damon.backend.entity.*;
import damon.backend.entity.user.User;
import damon.backend.exception.ReviewException;
import damon.backend.repository.ReviewImageRepository;
import damon.backend.repository.ReviewLikeRepository;
import damon.backend.repository.ReviewRepository;
import damon.backend.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {

    private final AwsS3Service awsS3Service;
    private final ReviewRepository reviewRepository;
    private final ReviewImageRepository reviewImageRepository;
    private final ReviewLikeRepository reviewLikeRepository;
    private final UserRepository userRepository;
    private final CommentStructureOrganizer commentStructureOrganizer;
    private final ReviewImageService reviewImageService;

    // 등록
    public ReviewResponse postReview(ReviewRequest request, String identifier) {
        User user = userRepository.findByIdentifier(identifier).orElseThrow(ReviewException::memberNotFound);

        Review review = Review.create(request.getTitle(), request.getStartDate(), request.getEndDate(),
                request.getArea(), request.getCost(), request.getSuggests(),
                request.getContent(), request.getTags(), user);
        review = reviewRepository.save(review);

        if (request.getImage() != null) {
            ReviewImage reviewImage =
                    reviewImageRepository.save(new ReviewImage(request.getImage(), true, review));
            review.addImage(reviewImage);
        }
        reviewRepository.save(review); // 이미지 URL이 추가된 리뷰 저장


        List<ReviewCommentResponse> emptyCommentsList = new ArrayList<>(); // 새 리뷰에는 댓글이 없으므로 빈 리스트 생성
        return ReviewResponse.from(review, emptyCommentsList); // 저장된 리뷰와 빈 댓글 목록을 전달
    }

    // 수정
    public ReviewResponse updateReview(Long reviewId, ReviewRequest request, List<MultipartFile> newImages, List<Long> imageIdsToDelete, String identifier)  {

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(ReviewException::reviewNotFound);

        User user = userRepository.findByIdentifier(identifier).orElseThrow(ReviewException::memberNotFound);

        if (!review.getUser().getIdentifier().equals(identifier)) {
            throw ReviewException.unauthorized();
        }

        // 리뷰 업데이트
        review.update(request.getTitle(), request.getStartDate(), request.getEndDate(), request.getArea(),
                request.getCost(), request.getSuggests(), request.getContent(), request.getTags());


        // 이미지 처리: 새 이미지 추가 및 기존 이미지 삭제
        reviewImageService.handleImage(review, newImages, imageIdsToDelete);
        review = reviewRepository.save(review);

        // 댓글 구조를 다시 조직화
        List<ReviewCommentResponse> organizedComments = commentStructureOrganizer.organizeCommentStructure(reviewId);

        // 구조화된 댓글 목록을 포함하여 ReviewResponse 반환
        return ReviewResponse.from(review, organizedComments);
    }

    // 상세 조회 (댓글 포함)
    @Transactional(readOnly = true)
    public ReviewResponse searchReviewDetail(Long reviewId, boolean incrementViewCount) {
        Review review = reviewRepository.findReviewWithCommentsAndRepliesByReviewId(reviewId)
                .orElseThrow(ReviewException::reviewNotFound);

        if (incrementViewCount) {
            review.incrementViewCount();
            reviewRepository.save(review);
        }

        // 댓글 구조화 로직을 사용하여 댓글 목록 가져오기 (상세 조회 시에만 호출)
        List<ReviewCommentResponse> organizedComments = commentStructureOrganizer.organizeCommentStructure(reviewId);

        // DTO 반환 댓글 목록 포함
        return ReviewResponse.from(review, organizedComments); // 이 부분에서 organizedComments를 DTO에 포함시켜야 합니다.
    }

    // 전체 및 지역별 조회
    @Transactional(readOnly = true)
    public Page<ReviewListResponse> searchReviewList(int page, int pageSize, Area area) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "id"));
        Page<Review> reviews = area != null ? reviewRepository.findByArea(area, pageable) : reviewRepository.findAll(pageable);
        return reviews.map(ReviewListResponse::from);
    }

    // 검색
    @Transactional(readOnly = true)
    public Page<ReviewListResponse> searchReviews(String searchMode, String keyword, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "id"));
        Page<Review> reviews = reviewRepository.searchByCriteria(searchMode, keyword, pageable);
        return reviews.map(ReviewListResponse::from);
    }

    // 삭제
    public void deleteReview(Long reviewId, String identifier) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(ReviewException::reviewNotFound);
        User user = userRepository.findByIdentifier(identifier).orElseThrow(ReviewException::memberNotFound);

        if (!review.getUser().getIdentifier().equals(identifier)) {
            throw ReviewException.unauthorized();
        }
        reviewRepository.delete(review);
    }

    // 좋아요 수 (다시 누르면 좋아요 취소)
    @Transactional
    public void toggleLike(Long reviewId, String identifier) {
        User user = userRepository.findByIdentifier(identifier).orElseThrow(ReviewException::memberNotFound);
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(ReviewException::reviewNotFound);

        Optional<ReviewLike> like = reviewLikeRepository.findByReviewAndUser(review, user);
        if (like.isPresent()) {
            review.decreaseLikeCount();
            reviewLikeRepository.delete(like.get());
        } else {
            ReviewLike reviewLike = ReviewLike.createLike(review, user);
            review.increaseLikeCount();
            reviewLikeRepository.save(reviewLike);
        }
    }

    // 좋아요 누른 게시글 조회
    @Transactional(readOnly = true)
    public Page<ReviewListResponse> searchLikedReviews(String identifier, int page, int pageSize) {
        User user = userRepository.findByIdentifier(identifier).orElseThrow(ReviewException::memberNotFound);

        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "id"));
        Page<Review> likedReviews = reviewLikeRepository.findReviewsByUser(user, pageable);
        return likedReviews.map(ReviewListResponse::from);
    }


    // 메인 페이지 베스트 리뷰 조회
    @Transactional(readOnly = true)
    public List<ReviewListResponse> findTopReviewsForMainPage(int size) {
        Pageable topFive = PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "likeCount"));
        Page<Review> reviews = reviewRepository.findAll(topFive); // findAll 메서드 사용 시 정렬 조건에 likeCount를 사용
        return reviews.getContent()
                .stream()
                .map(ReviewListResponse::from)
                .collect(Collectors.toList());
    }

}
