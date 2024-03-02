package damon.backend.service;

import damon.backend.dto.response.community.CommunityCommentDTO;
import damon.backend.dto.response.community.CommunityDetailDTO;
import damon.backend.dto.response.community.CommunitySimpleDTO;
import damon.backend.entity.community.Community;
import damon.backend.entity.user.User;
import damon.backend.enums.CommunityType;
import damon.backend.exception.custom.DataNotFoundException;
import damon.backend.repository.community.CommunityCommentRepository;
import damon.backend.repository.community.CommunityLikeRepository;
import damon.backend.repository.community.CommunityRepository;
import damon.backend.repository.user.UserRepository;
import damon.backend.util.Log;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@SpringBootTest
@Transactional
class CommunityServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommunityRepository communityRepository;

    @Autowired
    private CommunityCommentRepository commentRepository;

    @Autowired
    private CommunityLikeRepository likeRepository;

    @Autowired
    private CommunityService communityService;

    private User user1;
    private User user2;

    private Community community1;
    private Community community2;

    @BeforeEach
    void beforeEach() {
        // given
        user1 = userRepository.save(new User("1", "사용자1", "ex1@naver.com", "http://ex1.png"));
        user2 = userRepository.save(new User("2", "사용자2", "ex2@naver.com", "http://ex2.png"));

        community1 = communityRepository.save(new Community(user1, CommunityType.번개, "community1 title", "community1 content"));
        community2 = communityRepository.save(new Community(user2, CommunityType.자유, "community2 title", "community2 content"));
    }

    @Test
    @DisplayName("커뮤니티 정보 조회")
    void getCommunity() {
        // when
        CommunityDetailDTO dto = communityService.getCommunity(community1.getCommunityId());

        // then
        assertAll(
                () -> assertEquals(dto.getCommunityId(), community1.getCommunityId()),
                () -> assertEquals(dto.getTitle(), community1.getTitle()),
                () -> assertEquals(dto.getContent(), community1.getContent())
        );

    }

    @Test
    @DisplayName("커뮤니티 전체 리스트 조회")
    void getCommunityList() {
        // when
        Community community3 = communityRepository.save(new Community(user1, CommunityType.번개, "community3 title", "community3 content"));
        Community community4 = communityRepository.save(new Community(user1, CommunityType.번개, "community4 title", "community4 content"));

        List<CommunitySimpleDTO> list1 = communityService.getCommunityList(CommunityType.번개);
        List<CommunitySimpleDTO> list2 = communityService.getCommunityList(CommunityType.자유);

        // then
        assertAll(
                () -> assertEquals(list1.size(), 3),
                () -> assertEquals(list2.size(), 1),
                () -> assertEquals(list1.get(0).getCommunityId(), community4.getCommunityId()),
                () -> assertEquals(list1.get(0).getTitle(), community4.getTitle())
        );
    }

    @Test
    @DisplayName("커뮤니티 전체 페이징 조회")
    void getCommunityPaging() {
        // when
        Community community3 = communityRepository.save(new Community(user1, CommunityType.번개, "community3 title", "community3 content"));
        Community community4 = communityRepository.save(new Community(user1, CommunityType.번개, "community4 title", "community4 content"));

        Page<CommunitySimpleDTO> page1 = communityService.getCommunityPaging(CommunityType.번개, 0);
        Page<CommunitySimpleDTO> page2 = communityService.getCommunityPaging(CommunityType.자유, 0);

        // then
        assertAll(
                () -> assertEquals(page1.getContent().size(), 3), // 조회된 데이터 수
                () -> assertEquals(page2.getContent().size(), 1), // 조회된 데이터 수
                () -> assertEquals(page1.getTotalElements(), 3), // 전체 데이터 수
                () -> assertEquals(page1.getNumber(), 0), // 현재 페이지 번호
                () -> assertEquals(page1.getTotalPages(), 1), // 전체 페이지 수
                () -> assertTrue(page1.isFirst()), // 첫번째 항목인가
                () -> assertFalse(page1.hasNext()) // 다음 페이지가 있는가
        );
    }

    @Test
    @DisplayName("커뮤니티 최근 5개 리스트 조회")
    void getCommunityTop5() {
        // when
        Community community3 = communityRepository.save(new Community(user1, CommunityType.번개, "community3 title", "community3 content"));
        Community community4 = communityRepository.save(new Community(user1, CommunityType.번개, "community4 title", "community4 content"));
        Community community5 = communityRepository.save(new Community(user1, CommunityType.번개, "community5 title", "community5 content"));
        Community community6 = communityRepository.save(new Community(user1, CommunityType.번개, "community6 title", "community6 content"));
        Community community7 = communityRepository.save(new Community(user1, CommunityType.번개, "community7 title", "community7 content"));

        List<CommunitySimpleDTO> list1 = communityService.getCommunityTop5(CommunityType.번개);
        List<CommunitySimpleDTO> list2 = communityService.getCommunityTop5(CommunityType.자유);

        // then
        assertAll(
                () -> assertEquals(list1.size(), 5),
                () -> assertEquals(list2.size(), 1),
                () -> assertEquals(list1.get(0).getCommunityId(), community7.getCommunityId())
        );
    }

    @Test
    @DisplayName("내가 작성한 커뮤니티 페이징 조회")
    void getMyCommunityPaging() {
        // when
        Page<CommunitySimpleDTO> page1 = communityService.getMyCommunityPaging(user1.getIdentifier(), CommunityType.번개, 0);
        Page<CommunitySimpleDTO> page2 = communityService.getMyCommunityPaging(user1.getIdentifier(), CommunityType.자유, 0);

        // then
        assertAll(
                () -> assertEquals(page1.getContent().size(), 1), // 조회된 데이터 수
                () -> assertEquals(page2.getContent().size(), 0) // 조회된 데이터 수
        );
    }

    @Test
    @DisplayName("커뮤니티 추가")
    void addCommunity() {
        // when
        Long communityId = communityService.addCommunity(user1.getIdentifier(), CommunityType.번개, "community3 title", "community3 content").getCommunityId();
        CommunityDetailDTO dto = communityService.getCommunity(communityId);

        // then
        assertAll(
                () -> assertEquals(dto.getTitle(), "community3 title"),
                () -> assertEquals(dto.getContent(), "community3 content")
        );
    }

    @Test
    @DisplayName("커뮤니티 수정")
    void setCommunity() {
        // when
        communityService.setCommunity(community1.getCommunityId(), "update title", "update content");
        CommunityDetailDTO dto = communityService.getCommunity(community1.getCommunityId());

        // then
        assertAll(
                () -> assertEquals(dto.getTitle(), "update title"),
                () -> assertEquals(dto.getContent(), "update content")
        );
    }

    @Test
    @DisplayName("커뮤니티 제거")
    void removeCommunity() {
        // when
        communityService.removeCommunity(community1.getCommunityId());

        // then
        assertThrows(DataNotFoundException.class, () -> {
            communityService.getCommunity(community1.getCommunityId());
        });
    }

    @Test
    @DisplayName("커뮤니티 댓글 추가")
    void addComment() {
        // when
        communityService.addComment(user1.getIdentifier(), community1.getCommunityId(), "comment1 content");
        communityService.addComment(user1.getIdentifier(), community1.getCommunityId(), "comment2 content");
        communityService.addComment(user1.getIdentifier(), community1.getCommunityId(), "comment3 content");
        CommunityCommentDTO dto = communityService.getCommunity(community1.getCommunityId()).getComments().get(0);

        // then
        assertEquals(dto.getContent(), "comment3 content");
    }

    @Test
    @DisplayName("커뮤니티 댓글 수정")
    void setComment() {
        // when
        Long commentId = communityService.addComment(user1.getIdentifier(), community1.getCommunityId(), "comment1 content").getCommentId();
        communityService.setComment(commentId, "update content");
        CommunityCommentDTO dto = communityService.getCommunity(community1.getCommunityId()).getComments().get(0);

        // then
        assertEquals(dto.getContent(), "update content");
    }

    @Test
    @DisplayName("커뮤니티 댓글 제거")
    void removeComment() {
        // when
        Long commentId = communityService.addComment(user1.getIdentifier(), community1.getCommunityId(), "comment1 content").getCommentId();
        communityService.removeComment(commentId);

        // then
        assertEquals(communityService.getCommunity(community1.getCommunityId()).getComments().size(), 0);
    }

    @Test
    @DisplayName("커뮤니티 대댓글 추가")
    void addChildComment() {
        // comment1 content
        //  ㄴ childComment1 content, childComment2 content
        //       ㄴ child of child content

        // when
        Long commentId = communityService.addComment(user1.getIdentifier(), community1.getCommunityId(), "comment1 content").getCommentId();
        Long childCommentId = communityService.addChildComment(user1.getIdentifier(), commentId, "childComment1 content").getCommentId(); // 대댓글
        communityService.addChildComment(user1.getIdentifier(), commentId, "childComment2 content");

        communityService.addChildComment(user1.getIdentifier(), childCommentId, "child of child content"); // 대대댓글

        CommunityCommentDTO dto = communityService.getCommunity(community1.getCommunityId()).getComments().get(0); // comment1 content

        // then
        assertAll(
                () -> assertEquals(dto.getChildComments().get(0).getContent(), "childComment1 content"),
                () -> assertEquals(dto.getChildComments().get(0).getChildComments().get(0).getContent(), "child of child content")
        );
    }

    @Test
    @DisplayName("커뮤니티 댓글 제거")
    void removeChildComment() {
        // when
        Long commentId = communityService.addComment(user1.getIdentifier(), community1.getCommunityId(), "comment1 content").getCommentId();
        communityService.addChildComment(user1.getIdentifier(), commentId, "childComment1 content"); // 대댓글

        CommunityCommentDTO dto = communityService.getCommunity(community1.getCommunityId()).getComments().get(0).getChildComments().get(0); // 대댓글

        communityService.removeComment(commentId);

        // then
        assertEquals(communityService.getCommunity(community1.getCommunityId()).getComments().size(), 0); // 하위 레벨의 댓글도 같이 삭제 됨.
    }

    @Test
    @DisplayName("커뮤니티 좋아요")
    void like() {
        // when
        communityService.toggleLike(user1.getIdentifier(), community1.getCommunityId());
        communityService.toggleLike(user1.getIdentifier(), community2.getCommunityId());
        communityService.toggleLike(user1.getIdentifier(), community2.getCommunityId());

        // then
        assertAll(
                () -> assertTrue(communityService.isLike(user1.getIdentifier(), community1.getCommunityId())),
                () -> assertFalse(communityService.isLike(user2.getIdentifier(), community1.getCommunityId())),
                () -> assertFalse(communityService.isLike(user1.getIdentifier(), community2.getCommunityId())),
                () -> assertFalse(communityService.isLike(user2.getIdentifier(), community2.getCommunityId()))
        );
    }

    @Test
    @DisplayName("커뮤니티 검색")
    void searchCommunity() {
        // when
        Community community3 = communityRepository.save(new Community(user1, CommunityType.번개, "community3 title", "community3 content"));
        Community community4 = communityRepository.save(new Community(user1, CommunityType.번개, "community4 title", "community4 content"));
        Community community5 = communityRepository.save(new Community(user1, CommunityType.번개, "community5 title", "community5 content"));

        Page<CommunitySimpleDTO> page1 = communityService.searchCommunity(null, null, 0);
        Page<CommunitySimpleDTO> page2 = communityService.searchCommunity(null, CommunityType.번개, 0);
        Page<CommunitySimpleDTO> page3 = communityService.searchCommunity("community1 title", CommunityType.번개, 0);
        Page<CommunitySimpleDTO> page4 = communityService.searchCommunity("community1 content", CommunityType.번개, 0);

        // then
        assertAll(
                () -> assertEquals(page1.getContent().size(), 5),
                () -> assertEquals(page2.getContent().size(), 4),
                () -> assertEquals(page3.getContent().size(), 1),
                () -> assertEquals(page4.getContent().size(), 1)
        );
    }
}