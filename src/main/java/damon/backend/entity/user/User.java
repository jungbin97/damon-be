package damon.backend.entity.user;

import damon.backend.entity.Review;
import damon.backend.entity.ReviewComment;
import damon.backend.entity.ReviewLike;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "identifier")
    private String identifier;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "email")
    private String email;

    @Column(name = "profile")
    private String profile;

    public User(String identifier, String nickname, String email, String profile) {
        this.identifier = identifier;
        this.nickname = nickname;
        this.email = email;
        this.profile = profile;
    }

    //리뷰 매핑
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    //리뷰 댓글 매핑
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ReviewComment> reviewComments = new HashSet<>();

    //리뷰 좋아요 매핑
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewLike> reviewLikes = new ArrayList<>();

    // 회원 정보 신규등록
    public void createInfo(String identifier, String nickname, String email, String profile) {
        this.identifier = identifier;
        this.nickname = nickname;
        this.email = email;
        this.profile = profile;
    }

    // 회원 정보 업데이트
    public void updateInfo(String nickname, String profile) {
        this.nickname = nickname;
        this.profile = profile;
    }
}
