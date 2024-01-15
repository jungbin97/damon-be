package damon.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewComment {
    //not null 이 너무 많아서 기본값을 not null로 설정
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface NotNull {
        boolean nullable() default false;
    }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_comment_id")
    private Long id;
    private ZonedDateTime createTime;
    private ZonedDateTime updateTime;

    @Column(columnDefinition = "TEXT")
    private String content;

    //리뷰id 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    //부모댓글참조 (대댓글일 경우의 자기참조)
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private ReviewComment parent;

    //자식댓글목록 (대댓글)
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewComment> replies = new ArrayList<>();


    public void setReview(Review review){
        this.review = review;
        if (review != null ) {
            review.getReviewComments().add(this);
        }
    }

    //연관관계 매핑 메서드
    public void setParent(ReviewComment parent){
        this.parent = parent;
        if (parent != null) {
            parent.getReplies().add(this);
        }
    }

    public void addReply(ReviewComment reply){
        this.replies.add(reply);
        reply.setParent(this);
    }
}
