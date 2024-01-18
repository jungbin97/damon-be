package damon.backend.dto.request;

import lombok.Data;

@Data
public class ReviewCommentRequest {

    // private Long reviewId; 댓글이 달릴 리뷰의 ID
    // -> 이거 그냥 서비스에서 인자로 받겠음

    private Long parentId; // 대댓글의 경우 상위 댓글 ID, null 가능
    private String content;

}
