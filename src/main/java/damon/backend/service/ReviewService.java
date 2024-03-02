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
import java.util.ArrayList;
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

//    //게시글 등록
//    public ReviewResponse addReview(ReviewAndImageRequest form, String identifier) {
//        User user = userRepository.findByIdentifier(identifier).orElseThrow(ReviewException::memberNotFound);
//
//        Review review = reviewRepository.save(new Review(
//                form.getTitle(),
//                form.getStartDate(),
//                form.getEndDate(),
//                form.getArea(),
//                form.getCost(),
//                form.getSuggests(),
//                form.getFreeTags(),
//                form.getContent(),
//                user
//            )
//        );
//
//        // 이미지 정보 추가
//        if (form.getImage() != null) {
//            ReviewImage reviewImage =
//                    reviewImageRepository.save(new ReviewImage(form.getImage(), true, review));
//            review.addImage(reviewImage);
//        }
//
//        return new ReviewResponse(review);
//    }

    //게시글 등록
    public ReviewResponse postReview(ReviewRequest request, String identifier) {
        User user = userRepository.findByIdentifier(identifier).orElseThrow(ReviewException::memberNotFound);

        Review review = Review.create(request, user);
        review = reviewRepository.save(review); // 리뷰 저장

        // 이미지 처리
//        if (images != null && !images.isEmpty()) {
//            for (MultipartFile file : images) {
//                try {
//                    String url = awsS3Service.uploadImage(file); // S3에 이미지 업로드
//                    review.addImage(url); // 리뷰에 이미지 URL 추가
//                } catch (IOException e) {
//                    throw ReviewException.imageUploadFailed();
//                }
//            }
//        }

        if (request.getImage() != null) {
            ReviewImage reviewImage =
                    reviewImageRepository.save(new ReviewImage(request.getImage(), true, review));
            review.addImage(reviewImage);
        }
        reviewRepository.save(review); // 이미지 URL이 추가된 리뷰 저장


        List<ReviewCommentResponse> emptyCommentsList = new ArrayList<>(); // 새 리뷰에는 댓글이 없으므로 빈 리스트 생성
        return ReviewResponse.from(review, emptyCommentsList); // 저장된 리뷰와 빈 댓글 목록을 전달
    }

    // 게시글 전체 조회
    @Transactional(readOnly = true)
    public List<ReviewListResponse> searchReviewList(int page, int pageSize, Area area) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "id"));
        Page<Review> reviewPage;

        if (area != null) {
            reviewPage = reviewRepository.findByArea(area, pageable);
        } else {
            reviewPage = reviewRepository.findAll(pageable);
        }
        if (reviewPage.hasContent()) {
            return reviewPage.map(ReviewListResponse::from).toList();
        } else {
            return new ArrayList<>();
        }
    }

//    // 내 게시글 전체 조회
//    @Transactional(readOnly = true)
//    public List<ReviewListResponse> searchMyReviewList(String identifier) {
//        User user = userRepository.findByIdentifier(identifier).orElseThrow(ReviewException::memberNotFound);
//        List<Review> myReviews = reviewRepository.findMyReviews(user.getId());
//        return myReviews.stream().map(ReviewListResponse::from).toList();
//    }

    //게시글 상세 내용 조회 (댓글 포함)
    @Transactional(readOnly = true)
    public ReviewResponse searchReview(Long reviewId) {
        Review review = reviewRepository.findReviewWithCommentsAndRepliesByReviewId(reviewId)
                .orElseThrow(ReviewException::reviewNotFound);

        // 댓글 구조화 로직을 사용하여 댓글 목록 가져오기 (상세 조회 시에만 호출)
        List<ReviewCommentResponse> organizedComments = commentStructureOrganizer.organizeCommentStructure(reviewId);

        // DTO 반환 댓글 목록 포함
        return ReviewResponse.from(review, organizedComments); // 이 부분에서 organizedComments를 DTO에 포함시켜야 합니다.
    }

    // 조회수 로직
    public void incrementReviewViewCount(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(ReviewException::reviewNotFound);

        review.incrementViewCount(); // 조회수 증가
        reviewRepository.save(review); // 변경 사항 저장

    }

    // 게시글 수정
    public ReviewResponse updateReview(Long reviewId, ReviewRequest request, List<MultipartFile> newImages, List<Long> imageIdsToDelete, String identifier)  {

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(ReviewException::reviewNotFound);

        User user = userRepository.findByIdentifier(identifier).orElseThrow(ReviewException::memberNotFound);

        if (!review.getUser().getIdentifier().equals(identifier)) {
            throw ReviewException.unauthorized();
        }

        // 리뷰 업데이트
        review.update(request);

        // 이미지 처리: 새 이미지 추가 및 기존 이미지 삭제
        reviewImageService.handleImage(review, newImages, imageIdsToDelete);

        review = reviewRepository.save(review);

        // 댓글 구조를 다시 조직화
        List<ReviewCommentResponse> organizedComments = commentStructureOrganizer.organizeCommentStructure(reviewId);

        // 구조화된 댓글 목록을 포함하여 ReviewResponse 반환
        return ReviewResponse.from(review, organizedComments);
    }

    //게시글 삭제
    public void deleteReview(Long reviewId, String identifier) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(ReviewException::reviewNotFound);
        User user = userRepository.findByIdentifier(identifier).orElseThrow(ReviewException::memberNotFound);

        if (!review.getUser().getIdentifier().equals(identifier)) {
            throw ReviewException.unauthorized();
        }
        reviewRepository.delete(review);
    }


    //좋아요 수 계산 (다시 누르면 좋아요 취소)
    @Transactional
    public void toggleLike(Long reviewId, String identifier) {
        User user = userRepository.findByIdentifier(identifier).orElseThrow(ReviewException::memberNotFound);
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(ReviewException::reviewNotFound);

        Optional<ReviewLike> existingLike = reviewLikeRepository.findByReviewAndUser(review, user);
        if (existingLike.isPresent()) {
            reviewLikeRepository.delete(existingLike.get()); // 좋아요 제거
            review.decrementLikeCount(); // Review 엔티티 내 좋아요 수 감소 메서드
        } else {
            ReviewLike newLike = new ReviewLike();
            newLike.setReview(review);
            newLike.setUser(user);
            reviewLikeRepository.save(newLike); // 좋아요 추가
            review.incrementLikeCount(); // Review 엔티티 내 좋아요 수 증가 메서드
        }
    }

    // 좋아요 누른 게시글 모아보기
    @Transactional(readOnly = true)
    public List<ReviewListResponse> findLikedReviewsByUser(String identifier, int page, int pageSize) {
        User user = userRepository.findByIdentifier(identifier).orElseThrow(ReviewException::memberNotFound);

        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "id"));

        Page<ReviewLike> likedReviewsPage = reviewLikeRepository.findByUser(user, pageable);

        return likedReviewsPage.stream()
                .map(reviewLike -> ReviewListResponse.from(reviewLike.getReview()))
                .collect(Collectors.toList());
    }


    // 리뷰 검색
    @Transactional(readOnly = true)
    public List<ReviewListResponse> searchReviews(String searchMode, String keyword, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "id"));
        Page<Review> reviews = reviewRepository.searchByCriteria(searchMode, keyword, pageable);
        return reviews.map(ReviewListResponse::from)
                .getContent();
    }

    // 메인 페이지 베스트 리뷰 조회
    @Transactional(readOnly = true)
    public List<ReviewListResponse> findTopReviewsForMainPage(int size) {
        Pageable topFive = PageRequest.of(0, size, Sort.unsorted());
        Page<Review> reviews = reviewRepository.findTopReviewsByLikes(topFive);
        return reviews.stream()
                .map(ReviewListResponse::from)
                .collect(Collectors.toList());
    }

}
