package damon.backend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "community_comment")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommunityComment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "community_comment_id")
    private Long commentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "community_id")
    private Community community;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "community_comment_content")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "community_comment_parent_id")
    private CommunityComment parentComment;

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<CommunityComment> childComments = new ArrayList<>();

    public void setCommunityComment(String content) {
        this.content = content;
        this.lastModifiedDate = LocalDateTime.now(); // 마지막 수정 시간
    }

    public void deleteCommunityComment(Long commentId) {
        this.getCommunity().getComments().remove(this);
    }

    public CommunityComment(Community community, Member member, String content) {
        this.community = community;
        this.member = member;
        this.content = content;
    }

    public CommunityComment(Community community, Member member, String content, CommunityComment parentComment) {
        this.community = community;
        this.member = member;
        this.content = content;
        this.parentComment = parentComment;
    }

    // 대댓글 추가
    public void addChildComment(CommunityComment communityComment) {
        this.childComments.add(communityComment);
    }

    // 대댓글 수정
    public void setChildComment(CommunityComment childComment, String newContent) {
        if (childComments.contains(childComment)) {
            childComment.setCommunityComment(newContent);
        }
    }

    // 대댓글 제거
    public void removeChildComment(CommunityComment childComment) {
        childComments.remove(childComment);
    }
}
