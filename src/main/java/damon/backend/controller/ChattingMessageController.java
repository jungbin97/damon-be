package damon.backend.controller;

import damon.backend.dto.request.chatting.ChatMessageRequestDTO;
import damon.backend.dto.response.chatting.ChatMessageResponseDTO;
import damon.backend.dto.response.chatting.ChatRoomNotificationDTO;
import damon.backend.dto.request.chatting.CreateRoomDTO;
import damon.backend.model.ChatMessageModel;
import damon.backend.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChattingMessageController {
    private final SimpMessagingTemplate template;
    private final ChatService chatServiceImpl;


    /**
     * 채팅방 생성
     * @param createRoomDTO : 채팅방 생성에 필요한 정보(상대방 ID)
     * @param headerAccessor : 웹소켓 세션 정보(내 정보가 담겨있음)
     */
    @MessageMapping(value = "/chat/createRoom")
    public void create(@Payload CreateRoomDTO createRoomDTO, SimpMessageHeaderAccessor headerAccessor) {
        // 세션에서 현재 사용자의 ID를 가져옵니다.
//        String currentUserId = headerAccessor.getSessionAttributes().get("identifier").toString();
        String currentUserId = "test";

        // 채팅방 생성 로직을 호출하고, 채팅방 ID를 반환 받습니다.
        String roomId = chatServiceImpl.createChatRoom(currentUserId, createRoomDTO.getOpponentId());

        // 채팅방 토픽 경로 생성
        String chatRoomTopic = "/topic/chat/rooms/" + roomId;

        log.info("Chat Room Topic: {}", chatRoomTopic);
        // 메시지를 현재 사용자에게만 보내 채팅방 토픽을 구독하라고 알림
        template.convertAndSendToUser(currentUserId, "/queue/chat/roomCreated", ChatRoomNotificationDTO.of(roomId, chatRoomTopic));
    }

    /**
     * 채팅 메시지 전송, 채팅방의 토픽으로 메시지를 전송합니다.
     * @param roomId : 채팅방 ID
     * @param chatMessage : 전송할 메시지
     * @param headerAccessor : 웹소켓 세션 정보(내 정보가 담겨있음)
     */
    @MessageMapping(value = "/chat/{roomId}/sendMessage")
    public void sendMessage(@DestinationVariable String roomId, @Payload ChatMessageRequestDTO chatMessage, SimpMessageHeaderAccessor headerAccessor) {
//        String currentUserId = headerAccessor.getSessionAttributes().get("identifier").toString();
        String currentUserId = "test";

        // 메시지를 데이터베이스에 저장
        ChatMessageModel savedMessage = chatServiceImpl.saveMessage(roomId, chatMessage);

        // 채팅방의 토픽으로 메시지 전송
        String chatRoomTopic = "/topic/chat/rooms/" + roomId;

        // 메시지를 채팅방의 모든 구독자에게 전송
        template.convertAndSend(chatRoomTopic, ChatMessageResponseDTO.of(savedMessage.getSender(), savedMessage.getContent(), savedMessage.getTimestamp()));
    }
}
