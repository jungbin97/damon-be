package damon.backend.entity;

import damon.backend.enums.CommunityType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "community")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Community extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "community_id")
    private Long communityId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; // 작성자

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private CommunityType type; // [번개, 자유]

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "views")
    private int views;

    @ElementCollection
    @CollectionTable(name = "community_images", joinColumns = @JoinColumn(name = "community_id"))
    @Column(name = "community_image_path")
    private List<String> images = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "community", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CommunityLike> likes = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "community", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommunityComment> comments = new ArrayList<>();

    public void setCommunity(String title, String content) {
        this.title = title;
        this.content = content;
        this.lastModifiedDate = LocalDateTime.now(); // 마지막 수정 시간
    }

    public void setCommunity(String title, String content, List<String> images) {
        this.title = title;
        this.content = content;
        this.images = images;
        this.lastModifiedDate = LocalDateTime.now(); // 마지막 수정 시간
    }

    // 좋아요 여부
    public boolean isLike(Member member) {
        return likes.stream()
                .anyMatch(like -> like.getMember().getId().equals(member.getId()));
    }

    // 좋아요 추가
    public void addLike(Member member) {
        CommunityLike like = new CommunityLike(this, member);
        likes.add(like);
    }

    // 좋아요 제거
    public void removeLike(Member member) {
        CommunityLike findLike = likes.stream()
            .filter(like -> like.getMember().getId().equals(member.getId()))
            .findFirst()
            .orElse(null);

        if (findLike != null) {
            likes.remove(findLike);
        }
    }

    // 댓글 추가
    public CommunityComment addComment(Member member, String content) {
        CommunityComment comment = new CommunityComment(this, member, content);
        comments.add(comment);
        return comment;
    }

    // 댓글 수정
    public void setComment(CommunityComment comment, String newContent) {
        if (comments.contains(comment)) {
            comment.setCommunityComment(newContent);
        }
    }

    // 댓글 제거
    public void removeComment(CommunityComment comment) {
        comments.remove(comment);
    }

    public Community(Member member, CommunityType type, String title, String content) {
        this.member = member;
        this.type = type;
        this.title = title;
        this.content = content;
        this.views = 0;
    }
}