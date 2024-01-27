package damon.backend.dto.response.community;

import damon.backend.entity.CommunityComment;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class CommunityCommentDTO {

    private Long commentId;
    private String memberId;
    private String memberName;
    private String memberImage;
    private LocalDateTime createdDate;
    private String content;
    private List<CommunityCommentDTO> childComments;

    public CommunityCommentDTO(CommunityComment communityComment) {
        this.commentId = communityComment.getCommentId();
        this.memberId = communityComment.getMember().getId();
        this.memberName = communityComment.getMember().getNickname();
        this.memberImage = communityComment.getMember().getProfileImgUrl();
        this.createdDate = communityComment.getCreatedDate();
        this.content = communityComment.getContent();

        this.childComments = communityComment.getChildComments()
                .stream()
                .map(CommunityCommentDTO::new)
                .collect(Collectors.toList());
    }
}
