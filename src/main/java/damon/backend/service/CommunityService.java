//package damon.backend.service;
//
//import damon.backend.dto.response.community.CommunityCommentDTO;
//import damon.backend.dto.response.community.CommunityDetailDTO;
//import damon.backend.dto.response.community.CommunitySimpleDTO;
//import damon.backend.entity.Community;
//import damon.backend.entity.CommunityComment;
//import damon.backend.entity.CommunityLike;
//import damon.backend.entity.Member;
//import damon.backend.enums.CommunityType;
//import damon.backend.exception.DataNotFoundException;
//import damon.backend.exception.NotMeException;
//import damon.backend.repository.MemberRepository;
//import damon.backend.repository.community.CommunityCommentRepository;
//import damon.backend.repository.community.CommunityLikeRepository;
//import damon.backend.repository.community.CommunityRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//public class CommunityService {
//
//    private final CommunityRepository communityRepository;
//    private final CommunityCommentRepository commentRepository;
//    private final CommunityLikeRepository likeRepository;
//    private final MemberRepository memberRepository;
//
//    // 커뮤니티 전체 조회
//    public List<CommunitySimpleDTO> getCommunityList() {
//        List<Community> communities = communityRepository.findAllFetch();
//
//        return communities.stream()
//                .map(CommunitySimpleDTO::new)
//                .collect(Collectors.toList());
//    }
//
//    // 커뮤니티 페이징 조회
//    public Page<CommunitySimpleDTO> getCommunityPaging(CommunityType type, Pageable pageable) {
//        Page<Community> communities = communityRepository.findAllFetchPaging(type, pageable);
//
//        return communities.map(CommunitySimpleDTO::new);
//    }
//
//    // 커뮤니티 단건 조회
//    public CommunityDetailDTO getCommunity(Long communityId) {
//        Community community = communityRepository.findOneFetch(communityId)
//                .orElseThrow(() -> new DataNotFoundException("커뮤니티 정보가 없습니다. id : " + communityId));
//        return new CommunityDetailDTO(community);
//    }
//
//    // 커뮤니티 추가
//    public CommunitySimpleDTO addCommunity(String memberId, CommunityType type, String title, String content) {
//        Member member = memberRepository.findById(memberId)
//                .orElseThrow(() -> new DataNotFoundException("해당 사용자를 찾을 수 없습니다. id : " + memberId));
//
//        Community community = new Community(member, type, title, content);
//        communityRepository.save(community);
//        return new CommunitySimpleDTO(community);
//    }
//
//    // 커뮤니티 수정
//    public CommunitySimpleDTO setCommunity(String memberId, Long communityId, String title, String content, List<String> images) {
//        Member member = memberRepository.findById(memberId)
//                .orElseThrow(() -> new DataNotFoundException("해당 사용자를 찾을 수 없습니다. id : " + memberId));
//        Community community = communityRepository.findOneFetch(communityId)
//                .orElseThrow(() -> new DataNotFoundException("커뮤니티 정보가 없습니다. id : " + communityId));
//
//        // 본인 여부 검증
//        if (!memberId.equals(community.getMember().getId()))
//            throw new NotMeException();
//
//        community.setCommunity(title, content, images);
//        communityRepository.save(community);
//        return new CommunitySimpleDTO(community);
//    }
//
//    // 커뮤니티 제거
//    public void removeCommunity(Long communityId) {
//        Community community = communityRepository.findById(communityId)
//                .orElseThrow(() -> new DataNotFoundException("커뮤니티 정보가 없습니다. id : " + communityId));
//
//        communityRepository.delete(community);
//    }
//
//    // 커뮤니티에 댓글 추가
//    public CommunityCommentDTO addComment(String memberId, Long communityId, String content) {
//        Member member = memberRepository.findById(memberId)
//                .orElseThrow(() -> new DataNotFoundException("해당 사용자를 찾을 수 없습니다. id : " + memberId));
//        Community community = communityRepository.findOneFetch(communityId)
//                .orElseThrow(() -> new DataNotFoundException("커뮤니티 정보가 없습니다. id : " + communityId));
//
//        CommunityComment comment = community.addComment(member, content);
//        commentRepository.save(comment);
//        return new CommunityCommentDTO(comment);
//    }
//
//    // 커뮤니티에 댓글 or 대댓글 수정
//    public CommunityCommentDTO setComment(Long commentId, String newContent) {
//        CommunityComment comment = commentRepository.findById(commentId)
//                .orElseThrow(() -> new DataNotFoundException("커뮤니티 댓글 정보가 없습니다. id : " + commentId));
//
//        comment.setCommunityComment(newContent);
//        commentRepository.save(comment);
//
//        return new CommunityCommentDTO(comment);
//    }
//
//    // 커뮤니티에 댓글 제거
//    public void removeComment(Long commentId) {
//        CommunityComment comment = commentRepository.findById(commentId)
//                .orElseThrow(() -> new DataNotFoundException("커뮤니티 댓글 정보가 없습니다. id : " + commentId));
//
//        comment.deleteCommunityComment(comment.getCommentId());
//        commentRepository.delete(comment);
//    }
//
//    // 커뮤니티에 대댓글 추가
//    public CommunityCommentDTO addChildComment(String memberId, Long parentCommentId, String content) {
//        Member member = memberRepository.findById(memberId)
//                .orElseThrow(() -> new DataNotFoundException("해당 사용자를 찾을 수 없습니다. id : " + memberId));
//        CommunityComment parentComment = commentRepository.findOneFetch(parentCommentId)
//                .orElseThrow(() -> new DataNotFoundException("커뮤니티 댓글 정보가 없습니다. id : " + parentCommentId));
//
//        CommunityComment childComment = new CommunityComment(parentComment.getCommunity(), member, content, parentComment);
//        commentRepository.save(childComment);
//        parentComment.addChildComment(childComment);
//        return new CommunityCommentDTO(childComment);
//    }
//
//    // 커뮤니티에 대댓글 제거
//    public void removeChildComment(Long childCommentId) {
//        CommunityComment childComment = commentRepository.findOneFetch(childCommentId)
//                .orElseThrow(() -> new DataNotFoundException("커뮤니티 댓글 정보가 없습니다. id : " + childCommentId));
//        CommunityComment parentComment = childComment.getParentComment();
//
//        parentComment.removeChildComment(childComment);
//        commentRepository.delete(childComment);
//    }
//
//    // 커뮤니티에 좋아요 여부 확인
//    public boolean isLike(Long communityId, String memberId) {
//        Member member = memberRepository.findById(memberId)
//                .orElseThrow(() -> new DataNotFoundException("해당 사용자를 찾을 수 없습니다. id : " + memberId));
//        Community community = communityRepository.findOneFetch(communityId)
//                .orElseThrow(() -> new DataNotFoundException("커뮤니티 정보가 없습니다. id : " + communityId));
//
//        return community.isLike(member);
//    }
//
//    // 커뮤니티에 좋아요 추가
//    public void addLike(Long communityId, String memberId) {
//        Member member = memberRepository.findById(memberId)
//                .orElseThrow(() -> new DataNotFoundException("해당 사용자를 찾을 수 없습니다. id : " + memberId));
//        Community community = communityRepository.findOneFetch(communityId)
//                .orElseThrow(() -> new DataNotFoundException("커뮤니티 정보가 없습니다. id : " + communityId));
//
//        if (!community.isLike(member)) {
//            community.addLike(member);
//        }
//    }
//
//    // 커뮤니티에 좋아요 제거
//    public void removeLike(Long communityId, String memberId) {
//        Member member = memberRepository.findById(memberId)
//                .orElseThrow(() -> new DataNotFoundException("해당 사용자를 찾을 수 없습니다. id : " + memberId));
//        Community community = communityRepository.findOneFetch(communityId)
//                .orElseThrow(() -> new DataNotFoundException("커뮤니티 정보가 없습니다. id : " + communityId));
//
//        if (community.isLike(member)) {
//            community.removeLike(member);
//        }
//    }
//}