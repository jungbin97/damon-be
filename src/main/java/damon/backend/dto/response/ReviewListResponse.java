package damon.backend.dto.response;

import damon.backend.entity.Area;
import damon.backend.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
@AllArgsConstructor
public class ReviewListResponse {

    private Long id;
    private String name;
    private String state;
    private String createdDate;  // LocalDateTime -> String

    private long viewCount; // 조회수
    private long likeCount; // 좋아요 수
    private long commentCount; // 댓글 수

    private String title;
    private Area area;

    private Long cost;
    private List<String> suggests; // 장소 추천
    private List<String> freeTags; // 자유 태그

    private String mainImage;

    // 날짜 포맷터 정의
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    //정적 메소드
    public static ReviewListResponse from(Review review) {

        long viewCount = review.getViewCount(); // 조회수
        long likeCount = review.getReviewLikes().size(); // 좋아요 수
        long commentCount = review.getReviewComments().size(); // 댓글수

        String state = review.isEdited() ? "편집됨" : ""; // isEdited 값에 따라 상태 설정

        if (review.getReviewImages().size() > 0) {
            return new ReviewListResponse(

                    review.getId(),
                    review.getUser() != null ? review.getUser().getNickname() : null,
                    state,
                    review.getCreatedDate().format(DATE_TIME_FORMATTER),    // LocalDateTime -> String
                    review.getViewCount(),
                    likeCount,
                    commentCount,
                    review.getTitle(),
                    review.getArea(),
                    review.getCost(),
                    review.getSuggests(),
                    review.getFreeTags(),
                    review.getReviewImages().get(0).getUrl()
            );
        } else {
            return new ReviewListResponse(

                    review.getId(),
                    review.getUser() != null ? review.getUser().getNickname() : null,
                    state,
                    review.getCreatedDate().format(DATE_TIME_FORMATTER),    // LocalDateTime -> String
                    review.getViewCount(),
                    likeCount,
                    commentCount,
                    review.getTitle(),
                    review.getArea(),
                    review.getCost(),
                    review.getSuggests(),
                    review.getFreeTags(),
                    "");
        }

    }

}
