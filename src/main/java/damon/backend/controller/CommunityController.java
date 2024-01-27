package damon.backend.controller;

import damon.backend.dto.Result;
import damon.backend.dto.request.community.*;
import damon.backend.dto.response.community.CommunityCommentDTO;
import damon.backend.dto.response.community.CommunityDetailDTO;
import damon.backend.dto.response.community.CommunitySimpleDTO;
import damon.backend.enums.CommunityType;
import damon.backend.service.CommunityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@Tag(name = "커뮤니티 API", description = "커뮤니티 API")
@RestController
@RequestMapping("/api/community")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CommunityController {

    private final CommunityService communityService;

    @Operation(summary = "커뮤니티 페이징 조회", description = "자유, 번개에 따라 데이터를 페이징 처리해줍니다.")
    @GetMapping("/")
    public Result<Page<CommunitySimpleDTO>> getCommunityList(
            @Schema(description = "커뮤니티 타입(번개, 자유)", defaultValue = "번개")
            @RequestParam("type")CommunityType type,
            @Schema(description = "페이지 번호(0 ~ n)", defaultValue = "0")
            @RequestParam("page") int page // 0 부터 시작
//            @RequestParam("pageSize") int pageSize // 기본 20으로 지정
    ) {
        Pageable pageable = PageRequest.of(page, 20);
        Page<CommunitySimpleDTO> communityList = communityService.getCommunityPaging(type, pageable);
        return Result.success(communityList);
    }

    @Operation(summary = "커뮤니티 단건 조회", description = "커뮤니티 상세 화면에 사용되는 데이터를 반환해줍니다.")
    @GetMapping("/{communityId}")
    public Result<CommunityDetailDTO> getCommunity(
            @Schema(description = "커뮤니티 아이디", defaultValue = "1")
            @PathVariable Long communityId
    ) {
        CommunityDetailDTO communityDetail = communityService.getCommunity(communityId);
        return Result.success(communityDetail);
    }

    @Operation(summary = "커뮤니티 추가", description = "커뮤니티를 추가해줍니다.")
    @PostMapping("/")
    public Result<CommunitySimpleDTO> addCommunity(@RequestBody CommunityCreateForm createForm) {
        CommunitySimpleDTO addedCommunity = communityService.addCommunity(
                "1", // 멤버 아이디를 가져올 수 있어야함.
                createForm.getType(),
                createForm.getTitle(),
                createForm.getContent()
        );
        return Result.success(addedCommunity);
    }

    @Operation(summary = "커뮤니티 수정", description = "커뮤니티를 수정해줍니다.")
    @PutMapping("/")
    public Result<CommunitySimpleDTO> setCommunity(@RequestBody CommunityUpdateForm updateForm) {
        CommunitySimpleDTO updatedCommunity = communityService.setCommunity(
                "1", // 멤버 아이디를 가져올 수 있어야함.
                updateForm.getCommunityId(),
                updateForm.getTitle(),
                updateForm.getContent(),
                updateForm.getImages()
        );
        return Result.success(updatedCommunity);
    }

    @Operation(summary = "커뮤니티 삭제", description = "커뮤니티를 삭제해줍니다.")
    @DeleteMapping("/{communityId}")
    public Result<String> removeCommunity(
            @Schema(description = "커뮤니티 아이디", defaultValue = "1")
            @PathVariable Long communityId
    ) {
        communityService.removeCommunity(communityId);
        return Result.success("Community removed successfully");
    }

    // 댓글 추가
    @Operation(summary = "커뮤니티 댓글 추가", description = "커뮤니티 댓글을 추가해줍니다.")
    @PostMapping("/comment")
    public Result<CommunityCommentDTO> addComment(@RequestBody CommunityCommentCreateForm commentCreateForm) {
        CommunityCommentDTO addedComment = communityService.addComment(
                "1", // 멤버 아이디를 가져올 수 있어야함.
                commentCreateForm.getCommunityId(),
                commentCreateForm.getContent()
        );
        return Result.success(addedComment);
    }

    @Operation(summary = "커뮤니티 댓글 수정", description = "커뮤니티 댓글을 수정해줍니다.")
    @PutMapping("/comment")
    public Result<CommunityCommentDTO> setComment(@RequestBody CommunityCommentUpdateForm updateForm) {
        CommunityCommentDTO comment = communityService.setComment(
                updateForm.getCommentId(),
                updateForm.getContent()
        );
        return Result.success(comment);
    }

    // 댓글 제거
    @Operation(summary = "커뮤니티 댓글 제거", description = "커뮤니티 댓글을 제거해줍니다.")
    @DeleteMapping("/comment/{commentId}")
    public Result<String> removeComment(
            @Schema(description = "커뮤니티 댓글 아이디", defaultValue = "1")
            @PathVariable Long commentId
    ) {
        communityService.removeComment(commentId);
        return Result.success("Comment removed successfully");
    }

    // 대댓글 추가
    @Operation(summary = "커뮤니티 대댓글 추가", description = "커뮤니티 대댓글을 추가해줍니다.")
    @PostMapping("/comment/child")
    public Result<CommunityCommentDTO> addChildComment(@RequestBody CommunityChildCommentCreateForm form) {
        CommunityCommentDTO addedChildComment = communityService.addChildComment(
                "1", // 멤버 아이디를 가져올 수 있어야함.
                form.getParentId(),
                form.getContent()
        );
        return Result.success(addedChildComment);
    }

    // 대댓글 제거
    @Operation(summary = "커뮤니티 대댓글 제거", description = "커뮤니티 대댓글을 제거해줍니다.")
    @DeleteMapping("/comment/child/{childCommentId}")
    public Result<String> removeChildComment(
            @Schema(description = "커뮤니티 대댓글 아이디", defaultValue = "1")
            @PathVariable Long childCommentId
    ) {
        communityService.removeChildComment(childCommentId);
        return Result.success("Child Comment removed successfully");
    }

    // 좋아요 확인
    @Operation(summary = "커뮤니티 좋아요 확인", description = "커뮤니티 좋아요 상태인지 확인해줍니다.")
    @GetMapping("/like/{communityId}")
    public Result<Boolean> isLike(
            @Schema(description = "커뮤니티 아이디", defaultValue = "1")
            @PathVariable Long communityId
    ) {
        boolean result = communityService.isLike(communityId, "1");// 멤버 아이디를 가져올 수 있어야함.
        return Result.success(result);
    }

    // 좋아요 추가
    @Operation(summary = "커뮤니티 좋아요 추가", description = "커뮤니티 좋아요를 추가해줍니다.")
    @PostMapping("/like/{communityId}")
    public Result<String> addLike(
            @Schema(description = "커뮤니티 아이디", defaultValue = "1")
            @PathVariable Long communityId
    ) {
        communityService.addLike(communityId, "1"); // 멤버 아이디를 가져올 수 있어야함.
        return Result.success("Like added successfully");
    }

    // 좋아요 제거
    @Operation(summary = "커뮤니티 좋아요 제거", description = "커뮤니티 좋아요를 제거해줍니다.")
    @DeleteMapping("/like/{communityId}")
    public Result<String> removeLike(
            @Schema(description = "커뮤니티 아이디", defaultValue = "1")
            @PathVariable Long communityId
    ) {
        communityService.removeLike(communityId, "1"); // 멤버 아이디를 가져올 수 있어야함.
        return Result.success("Like removed successfully");
    }
}
