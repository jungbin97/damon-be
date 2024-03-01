package damon.backend.entity.community;

import damon.backend.entity.BaseEntity;
import damon.backend.entity.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 커뮤니티 좋아요 정보를 나타내는 엔티티 클래스입니다.
 */
@Entity
@Table(name = "community_like")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommunityLike extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "community_like_id")
    private Long likeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "community_id")
    private Community community;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public CommunityLike(Community community, User user) {
        this.community = community;
        this.user = user;
    }
}