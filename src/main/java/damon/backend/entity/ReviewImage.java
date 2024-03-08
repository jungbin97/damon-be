package damon.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Table(name = "review_image")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ReviewImage {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_image_id")

    private Long id;
    private String url;
    private String fileKey;

    //리뷰id 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;


    //연관관계 매핑 메서드
    public void setReview(Review review){
        this.review = review;
        if (review != null && !review.getReviewImages().contains(this)) {
            review.getReviewImages().add(this);
        }
    }


    // 이미지 생성
    public static ReviewImage createImage(String url, String fileKey, Review review) {
        ReviewImage image = new ReviewImage();
        image.url = url;
        image.fileKey = fileKey;
        image.setReview(review);
        return image;
    }
}