package damon.backend.controller;

import damon.backend.dto.Result;
import damon.backend.dto.response.chatting.ChatRoomsResponseDTO;
import damon.backend.model.ChatMessageModel;
import damon.backend.service.ChatService;
import damon.backend.util.login.AuthToken;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ChattingController {
    private final ChatService chatServiceImpl;

    /**
     * 1:1 채팅방 리스트 조회
     * @param identifier : 현재 사용자 ID
     * @return : 1:1 채팅방 리스트
     */
    @GetMapping("/user/chatrooms")
    public Result<List<ChatRoomsResponseDTO>> getUserRooms(
            @AuthToken String identifier) {
        List<ChatRoomsResponseDTO> chatRooms = chatServiceImpl.getUserRooms(identifier);

        return Result.success(chatRooms);
    }

    /**
     * 채팅방의 이전 메세지 리스트 조회 (페이징 처리)
     * @param roomId : 채팅방 ID
     * @param page : 페이지 번호
     * @param size : 페이지 크기
     * @return : 채팅방의 이전 메세지 목록
     */
    @GetMapping("/{roomId}/messages")
    public Result<List<ChatMessageModel>> getMessages(
            @PathVariable String roomId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        // 해당 채팅방의 이전 메세지 조회
        List<ChatMessageModel> messages = chatServiceImpl.getMessages(roomId, page, size);

        return Result.success(messages);
    }
}
