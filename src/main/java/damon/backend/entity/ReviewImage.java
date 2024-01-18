package damon.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Entity
@Getter
@Setter
public class ReviewImage {
    //not null 이 너무 많아서 기본값을 not null로 설정
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface NotNull {
        boolean nullable() default false;
    }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_image_id")

    private Long id;
    private String name;
    private String url;
    private boolean main;

    //리뷰id 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    //연관관계 매핑 메서드
    public void addReview(Review review){
        this.review = review;
        if (review != null ) {
            review.getReviewImages().add(this);
        }
    }


}
