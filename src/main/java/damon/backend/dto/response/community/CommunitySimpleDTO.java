package damon.backend.dto.response.community;

import damon.backend.entity.community.Community;
import damon.backend.enums.CommunityType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommunitySimpleDTO {

    private Long communityId;
    private Long userId; // 작성자 본인 여부 판단
    private String memberName;
    private String memberImage;
    private LocalDateTime createdDate;
    private CommunityType type;
    private String title;
    private int views;
    private int likesCount;
    private int commentsCount;

    public CommunitySimpleDTO(Community community) {
        this.communityId = community.getCommunityId();
        this.userId = community.getUser().getId();
        this.memberName = community.getUser().getNickname();
        this.memberImage = community.getUser().getProfile();
        this.createdDate = community.getCreatedDate();
        this.type = community.getType();
        this.title = community.getTitle();
        this.views = community.getViews();
        this.likesCount = community.getLikes().size();
        this.commentsCount = community.getComments().size();
    }
}
