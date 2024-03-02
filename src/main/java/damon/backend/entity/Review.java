package damon.backend.entity;

import damon.backend.dto.request.ReviewRequest;
import damon.backend.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.*;

@Entity
@Getter
@Table(name = "review")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    private long viewCount;

    private Long likeCount;
    private boolean isEdited = false; // 변경 여부를 추적하는 필드

    private String title;
    private LocalDate startDate;
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private Area area;

    private Long cost;

    @ElementCollection
    private List<String> suggests = new ArrayList<>();

    @Column(columnDefinition = "TEXT")
    private String content;

    //멤버id 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    //리뷰태그 매핑
    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Tag> tags = new ArrayList<>();

    //리뷰이미지 매핑
    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewImage> reviewImages = new ArrayList<>();

    //리뷰좋아요 매핑
    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ReviewLike> reviewLikes = new HashSet<>();

    //리뷰댓글 매핑
    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewComment> reviewComments = new ArrayList<>();

    //연관관계 매핑 메서드
    public void setUser(User user) {
        this.user = user;
        if (user != null) {
            user.getReviews().add(this);
        }
    }

    // 조회수 증가 메소드
    public void incrementViewCount() {
        this.viewCount++;
    }

    // 리뷰 생성 메서드
    public static Review create(ReviewRequest request, User user) {
        Review review = new Review();
        populateReviewFields(review, request);
        review.user = user; // Member 설정은 create 시에만 수행
        // 태그 설정
        if (request.getTags() != null && !request.getTags().isEmpty()) {
            for (String tagValue : request.getTags()) {
                review.getTags().add(new Tag(tagValue, review));
            }
        }
        return review;
    }

    // 리뷰 수정 메서드
    public void update(ReviewRequest request) {
        populateReviewFields(this, request);
        this.isEdited = true; // 업데이트 시 isEdited를 true로 설정

        // 기존 태그 모두 제거 후 새로운 태그 추가
        this.tags.clear();
        if (request.getTags() != null) {
            for (String tagValue : request.getTags()) {
                this.tags.add(new Tag(tagValue, this));
            }
        }
    }

    // 공통 필드 설정 메서드
    private static void populateReviewFields(Review review, ReviewRequest request) {
        review.title = request.getTitle();
        review.startDate = request.getStartDate();
        review.endDate = request.getEndDate();
        review.area = request.getArea();
        review.cost = request.getCost();
        review.suggests = request.getSuggests();
        review.content = request.getContent();

    }

    public void incrementLikeCount() {
        if (this.likeCount == null) {
            this.likeCount = 1L;
        } else {
            this.likeCount++;
        }
    }

    public void decrementLikeCount() {
        if (this.likeCount == null || this.likeCount <= 0) {
            this.likeCount = 0L;
        } else {
            this.likeCount--;
        }
    }

    // 이미지 추가 메서드
    public void addImage(String url) {
        ReviewImage newImage = ReviewImage.createImage(url, this); // URL을 받아 ReviewImage 객체 생성
        this.reviewImages.add(newImage); // 현재 리뷰의 이미지 목록에 추가
    }
//
//    public Review(String title, LocalDate startDate, LocalDate endDate, Area area, Long cost, List<String> suggests, List<Tag> tags, String content, User user) {
//        this.title = title;
//        this.startDate = startDate;
//        this.endDate = endDate;
//        this.area = area;
//        this.cost = cost;
//        this.suggests = suggests;
//        this.content = content;
//        this.user = user;
//    }

    public void addImage(ReviewImage reviewImage) {
        this.reviewImages.add(reviewImage);
        reviewImage.setReview(this); // 양방향 연관관계 설정
    }
}