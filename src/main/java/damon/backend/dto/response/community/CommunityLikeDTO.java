package damon.backend.dto.response.community;

import damon.backend.entity.CommunityLike;
import lombok.Data;

@Data
public class CommunityLikeDTO {

    private Long likeId;
    private Long communityId;
    private String memberId;

    public CommunityLikeDTO(CommunityLike communityLike) {
        this.likeId = communityLike.getLikeId();
        this.communityId = communityLike.getCommunity().getCommunityId();
        this.memberId = communityLike.getMember().getId();
    }
}
