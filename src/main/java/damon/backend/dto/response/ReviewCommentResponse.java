package damon.backend.dto.response;

import damon.backend.entity.ReviewComment;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class ReviewCommentResponse {

    private Long id;
    private ZonedDateTime createTime;
    private String state;
    private Long reviewId; // 대댓글일 경우에는 부모 댓글의 Id
    private Long parentId;
    private String content;
    private List<ReviewCommentResponse> replies; // 대댓글 목록


    // 정적 메소드
    public static ReviewCommentResponse from(ReviewComment reviewComment){
        // 대댓글 목록을 초기화하고, 재귀적으로 하위 댓글을 설정합니다.
        String state = "";
        if (reviewComment.getUpdateTime() != null && !reviewComment.getCreateTime().isEqual(reviewComment.getUpdateTime())) {
            state = "편집됨";
        }

        List<ReviewCommentResponse> replies = reviewComment.getReplies() != null ?
                reviewComment.getReplies().stream()
                        .map(replyComment -> {
                            ReviewCommentResponse replyResponse = from(replyComment);
                            // '편집됨' 상태를 각 대댓글에 대해 독립적으로 설정
                            if (replyComment.getUpdateTime() != null && !replyComment.getCreateTime().isEqual(replyComment.getUpdateTime())) {
                                replyResponse.setState("편집됨");
                            }
                            return replyResponse;
                        })
                        .collect(Collectors.toList()) : new ArrayList<>();


        return new ReviewCommentResponse(
                reviewComment.getId(),
                reviewComment.getCreateTime(),
                state,
                reviewComment.getReview() != null ? reviewComment.getReview().getId() : null, // 리뷰 ID
                reviewComment.getParent() != null ? reviewComment.getParent().getId() : null, // 부모 댓글 ID
                reviewComment.getContent(),
                replies
        );
    }

}
