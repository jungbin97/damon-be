package damon.backend.service;

import damon.backend.dto.response.community.CommunityCommentDTO;
import damon.backend.dto.response.community.CommunityDetailDTO;
import damon.backend.dto.response.community.CommunitySimpleDTO;
import damon.backend.entity.Community;
import damon.backend.entity.Member;
import damon.backend.enums.CommunityType;
import damon.backend.exception.DataNotFoundException;
import damon.backend.repository.MemberRepository;
import damon.backend.repository.community.CommunityCommentRepository;
import damon.backend.repository.community.CommunityLikeRepository;
import damon.backend.repository.community.CommunityRepository;
import damon.backend.util.Log;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

//@Slf4j
//@SpringBootTest
//@Transactional // 각 테스트 메서드가 종료될 때 롤백을 수행하여 데이터베이스에 영향을 주지 않도록
class CommunityServiceTest {

//    @PersistenceContext
//    EntityManager em;

//    @Autowired
//    private CommunityRepository communityRepository;
//
//    @Autowired
//    private CommunityCommentRepository commentRepository;
//
//    @Autowired
//    private CommunityLikeRepository likeRepository;
//
//    @Autowired
//    private MemberRepository memberRepository;
//
//    @Autowired
//    private CommunityService communityService;
//
//    private Member member;
//    private CommunityDetailDTO detailDTO;
//
//    @BeforeEach
//    void setting() {
//        // given
//        member = new Member();
//        member.setId("testId");
//        member.setNickname("testName");
//        member.setProfileImgUrl("/test");
//        memberRepository.save(member);
//
//        Long communityId = communityService.addCommunity(member.getId(), CommunityType.자유, "테스트 제목", "테스트 내용").getCommunityId();
//        Long commentId = communityService.addComment(member.getId(), communityId, "테스트 댓글").getCommentId();
//        communityService.addChildComment(member.getId(), commentId, "테스트 대댓글").getCommentId();
//        communityService.addLike(communityId, member.getId());
//        detailDTO = communityService.getCommunity(communityId);
//    }
//
//    @Test
//    @Disabled
//    void get() {
//        Log.info("@@" + communityRepository.findAllFetch().toString());
//    }
//
//    @Test
//    void getCommunityList() {
//        // when
//        communityService.addCommunity(member.getId(), CommunityType.자유, "추가 제목 1", "추가 내용 1");
//        communityService.addCommunity(member.getId(), CommunityType.자유, "추가 제목 2", "추가 내용 2");
//
//        // then
//        List<CommunitySimpleDTO> communityList = communityService.getCommunityList();
//        assertEquals(communityList.size(), 3);
//
//        Log.info(communityList);
//    }
//
//    @Test
//    void getCommunityListPaging() {
//        // when
//        communityService.addCommunity(member.getId(), CommunityType.자유, "추가 제목 1", "추가 내용 1");
//        communityService.addCommunity(member.getId(), CommunityType.자유, "추가 제목 2", "추가 내용 2");
//        communityService.addCommunity(member.getId(), CommunityType.자유, "추가 제목 3", "추가 내용 3");
//        communityService.addCommunity(member.getId(), CommunityType.자유, "추가 제목 4", "추가 내용 4");
//        communityService.addCommunity(member.getId(), CommunityType.자유, "추가 제목 5", "추가 내용 5");
//
//        Pageable pageable = PageRequest.of(0, 2);
//
//        Page<CommunitySimpleDTO> communityPage = communityService.getCommunityPaging(CommunityType.자유, pageable);
//        List<CommunitySimpleDTO> communityContent = communityPage.getContent(); // 조회된 데이터
//
//        // then
//        assertEquals(communityContent.size(), 2); // 조회된 데이터 수
//        assertEquals(communityPage.getTotalElements(), 6); // 전체 데이터 수
//        assertEquals(communityPage.getNumber(), 0); // 현재 페이지 번호
//        assertEquals(communityPage.getTotalPages(), 3); // 전체 페이지 번호
//        assertTrue(communityPage.isFirst()); // 첫번째 항목인가?
//        assertTrue(communityPage.hasNext()); // 다음 페이지가 있는가?
//
//        Page<CommunitySimpleDTO> communityPage2 = communityService.getCommunityPaging(CommunityType.번개, pageable);
//        assertEquals(communityPage2.getTotalElements(), 0); // 전체 데이터 수
//
//        Log.info(communityPage);
//        Log.info(communityContent);
//    }
//
//    @Test
//    void getCommunity() {
//        // when
//        CommunityDetailDTO tmpDetailResponse = communityService.getCommunity(detailDTO.getCommunityId());
//
//        // then
//        assertEquals(detailDTO, tmpDetailResponse);
//        Log.info(tmpDetailResponse);
//    }
//
//    @Test
//    void addCommunity() {
//        // when
//        Long communityId = communityService.addCommunity(
//                member.getId(),
//                CommunityType.자유,
//                "추가 제목 1",
//                "추가 내용 1"
//        ).getCommunityId();
//
//        CommunityDetailDTO communityDetailDTO = communityService.getCommunity(communityId);
//
//        // then
//        assertEquals(communityDetailDTO.getTitle(), "추가 제목 1");
//        assertEquals(communityDetailDTO.getContent(), "추가 내용 1");
//
//        Log.info(communityDetailDTO);
//    }
//
//    @Test
//    void setCommunity() {
//        // when
//        Long communityId = communityService.addCommunity(
//                member.getId(),
//                CommunityType.자유,
//                "추가 제목 1",
//                "추가 내용 1"
//        ).getCommunityId();
//
//        CommunityDetailDTO communityDetailDTO = communityService.getCommunity(communityId);
//        communityService.setCommunity(
//                communityDetailDTO.getMemberId(),
//                communityDetailDTO.getCommunityId(),
//                "수정 제목 1",
//                "수정 내용 1",
//                communityDetailDTO.getImages()
//        );
//
//        // then
//        assertEquals(communityService.getCommunity(communityId).getTitle(), "수정 제목 1");
//        assertEquals(communityService.getCommunity(communityId).getContent(), "수정 내용 1");
//
//        Log.info(communityService.getCommunity(communityId));
//    }
//
//    @Test
//    void removeCommunity() {
//        // when
//        Long communityId = communityService.addCommunity(
//                member.getId(),
//                CommunityType.자유,
//                "추가 제목 1",
//                "추가 내용 1"
//        ).getCommunityId();
//
//        Log.info(communityService.getCommunity(communityId));
//
//        communityService.removeCommunity(communityId);
//
//        // then
//        assertThrows(DataNotFoundException.class, () -> {
//            communityService.getCommunity(communityId);
//        });
//    }
//
//    @Test
//    void addComment() {
//        // when
//        Long communityId = communityService.addCommunity(
//                member.getId(),
//                CommunityType.자유,
//                "추가 제목 1",
//                "추가 내용 1"
//        ).getCommunityId();
//
//        CommunityDetailDTO communityDetailDTO = communityService.getCommunity(communityId);
//
//        CommunityCommentDTO communityCommentDTO = communityService.addComment(
//                communityDetailDTO.getMemberId(),
//                communityDetailDTO.getCommunityId(),
//                "추가 댓글 1"
//        );
//
//        // then
//        assertEquals(communityService.getCommunity(communityId).getComments().get(0).getContent(), "추가 댓글 1");
//        Log.info(communityCommentDTO);
//    }
//
//    @Test
//    void setComment() {
//        // when
//        Long communityId = communityService.addCommunity(
//                member.getId(),
//                CommunityType.자유,
//                "추가 제목 1",
//                "추가 내용 1"
//        ).getCommunityId();
//
//        CommunityDetailDTO communityDetailDTO = communityService.getCommunity(communityId);
//
//        CommunityCommentDTO communityCommentDTO = communityService.addComment(
//                communityDetailDTO.getMemberId(),
//                communityDetailDTO.getCommunityId(),
//                "추가 댓글 1"
//        );
//
//        communityService.setComment(communityCommentDTO.getCommentId(), "수정 댓글 1");
//
//        // then
//        assertEquals(communityService.getCommunity(communityId).getComments().get(0).getContent(), "수정 댓글 1");
//        Log.info(communityCommentDTO);
//    }
//
//    @Test
//    void removeComment() {
//        // when
//        Long communityId = communityService.addCommunity(
//                member.getId(),
//                CommunityType.자유,
//                "추가 제목 1",
//                "추가 내용 1"
//        ).getCommunityId();
//
//        CommunityDetailDTO communityDetailDTO = communityService.getCommunity(communityId);
//
//        CommunityCommentDTO communityCommentDTO = communityService.addComment(
//                communityDetailDTO.getMemberId(),
//                communityDetailDTO.getCommunityId(),
//                "추가 댓글 1"
//        );
//
//        // then
//        assertEquals(communityService.getCommunity(communityId).getComments().size(), 1);
//
//        // when
//        communityService.removeComment(communityCommentDTO.getCommentId());
//
//        // then
//        assertEquals(communityService.getCommunity(communityId).getComments().size(), 0);
//
//
//        Log.info(communityCommentDTO);
//    }
//
//    @Test
//    void addChildComment() {
//        // when
//        Long communityId = communityService.addCommunity(
//                member.getId(),
//                CommunityType.자유,
//                "추가 제목 1",
//                "추가 내용 1"
//        ).getCommunityId();
//
//        CommunityDetailDTO communityDetailDTO = communityService.getCommunity(communityId);
//
//        CommunityCommentDTO parent = communityService.addComment(
//                communityDetailDTO.getMemberId(),
//                communityDetailDTO.getCommunityId(),
//                "추가 댓글 1"
//        );
//
//        CommunityCommentDTO child = communityService.addChildComment(
//                communityDetailDTO.getMemberId(),
//                parent.getCommentId(),
//                "추가 대댓글 1"
//        );
//
//        // then
//        assertEquals(communityService.getCommunity(communityId).getComments().get(0).getChildComments().size(), 1);
//        assertEquals(communityService.getCommunity(communityId).getComments().get(0).getChildComments().get(0).getContent(), "추가 대댓글 1");
//        Log.info(communityService.getCommunity(communityId).getComments().get(0).getChildComments().get(0));
//        Log.info(child);
//    }
//
//    @Test
//    void removeChildComment() {
//        // when
//        Long communityId = communityService.addCommunity(
//                member.getId(),
//                CommunityType.자유,
//                "추가 제목 1",
//                "추가 내용 1"
//        ).getCommunityId();
//
//        CommunityDetailDTO communityDetailDTO = communityService.getCommunity(communityId);
//
//        CommunityCommentDTO parent = communityService.addComment(
//                communityDetailDTO.getMemberId(),
//                communityDetailDTO.getCommunityId(),
//                "추가 댓글 1"
//        );
//
//        CommunityCommentDTO child = communityService.addChildComment(
//                communityDetailDTO.getMemberId(),
//                parent.getCommentId(),
//                "추가 대댓글 1"
//        );
//
//        // then
//        assertEquals(communityService.getCommunity(communityId).getComments().get(0).getChildComments().size(), 1);
//        assertEquals(communityService.getCommunity(communityId).getComments().get(0).getChildComments().get(0).getContent(), "추가 대댓글 1");
//        Log.info(communityService.getCommunity(communityId).getComments().get(0).getChildComments().get(0));
//        Log.info(child);
//
//        // when
//        communityService.removeChildComment(child.getCommentId());
//
//        // then
//        assertEquals(communityService.getCommunity(communityId).getComments().get(0).getChildComments().size(), 0);
//    }
//
//    @Test
//    void isLike() {
//        // when
//        Long communityId = communityService.addCommunity(
//                member.getId(),
//                CommunityType.자유,
//                "추가 제목 1",
//                "추가 내용 1"
//        ).getCommunityId();
//
//        CommunityDetailDTO communityDetailDTO = communityService.getCommunity(communityId);
//
//        communityService.addLike(communityId, member.getId());
//
//        // then
//        assertTrue(communityService.isLike(communityId, member.getId()));
//        Log.info(communityService.getCommunity(communityId).getLikes().get(0));
//    }
//
//    @Test
//    void addLike() {
//        // when
//        Long communityId = communityService.addCommunity(
//                member.getId(),
//                CommunityType.자유,
//                "추가 제목 1",
//                "추가 내용 1"
//        ).getCommunityId();
//
//        CommunityDetailDTO communityDetailDTO = communityService.getCommunity(communityId);
//
//        communityService.addLike(communityId, member.getId());
//
//        // then
//        assertEquals(communityService.getCommunity(communityId).getLikes().size(), 1);
//        Log.info(communityService.getCommunity(communityId).getLikes().get(0));
//    }
//
//    @Test
//    void removeLike() {
//        // when
//        Long communityId = communityService.addCommunity(
//                member.getId(),
//                CommunityType.자유,
//                "추가 제목 1",
//                "추가 내용 1"
//        ).getCommunityId();
//
//        CommunityDetailDTO communityDetailDTO = communityService.getCommunity(communityId);
//
//        communityService.addLike(communityId, member.getId());
//
//        // then
//        assertEquals(communityService.getCommunity(communityId).getLikes().size(), 1);
//        Log.info(communityService.getCommunity(communityId).getLikes().get(0));
//
//        // when
//        communityService.removeLike(communityId, member.getId());
//
//        // then
//        assertEquals(communityService.getCommunity(communityId).getLikes().size(), 0);
//    }
}