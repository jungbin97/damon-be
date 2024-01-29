package damon.backend.dto.request.community;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CommunityCommentUpdateForm {

    @NotNull
    private Long commentId;

    @NotBlank(message = "댓글 내용을 입력해주세요.")
    private String content;
}
