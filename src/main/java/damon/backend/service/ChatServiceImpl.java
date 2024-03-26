package damon.backend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import damon.backend.dto.request.chatting.ChatMessageRequestDTO;
import damon.backend.dto.response.chatting.ChatRoomsResponseDTO;
import damon.backend.model.ChatMessageModel;
import damon.backend.model.ChatRoomModel;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatServiceImpl implements ChatService {
    private final RedisTemplate<String, Object> redisTemplate;
    private ListOperations<String, Object> listOps;
    private HashOperations<String, String, Object> hashOps;


    @PostConstruct
    private void init() {
        listOps = redisTemplate.opsForList();
        hashOps = redisTemplate.opsForHash();
    }

    private String generateUniqueRoomId() {
        return UUID.randomUUID().toString();
    }

    /**
     * 1:1 채팅방을 생성합니다.
     * @param participantOne : 참여자 1
     * @param participantTwo : 참여자 2
     * @return : 생성된 채팅방 ID
     */
    @Override
    public String createChatRoom(String participantOne, String participantTwo) {
        String roomId = generateUniqueRoomId();
        ChatRoomModel chatRoomModel = new ChatRoomModel(roomId, participantOne, participantTwo, LocalDateTime.now());

        // 채팅방 정보 저장 (해시 구조 사용)
        hashOps.put("chat:rooms", roomId, chatRoomModel);

        // 각 참여자의 "사용자별 채팅방 목록"에 채팅방 ID 저장
        redisTemplate.opsForSet().add("user:rooms:" + participantOne, roomId);
        redisTemplate.opsForSet().add("user:rooms:" + participantTwo, roomId);

        return roomId;
   }

    /**
     * 채팅 메시지를 저장합니다. (채팅방 ID를 이용하여 메시지를 저장)
     * @param roomId : 채팅방 ID
     * @param chatMessage : 채팅 메시지
     * @return : 저장된 채팅 메시지
     */
    @Override
    public ChatMessageModel saveMessage(String roomId, ChatMessageRequestDTO chatMessage) {
        ChatMessageModel chatMessageModel = new ChatMessageModel(chatMessage.getSender(), chatMessage.getContent(), LocalDateTime.now());

        // 메시지를 리스트 구조에 저장
        listOps.rightPush("chat:rooms:" + roomId, chatMessageModel);
        // 마지막 메시지 정보 저장
        hashOps.put("chat:rooms:lastMessage", roomId, chatMessageModel);

        // 저장한 메시지 반환
        return chatMessageModel;
    }

    /**
     * 채팅방의 이전 메세지를 조회합니다. (페이징 처리)
     * @param roomId : 채팅방 ID
     * @param page : 페이지 번호
     * @param size : 페이지 크기
     * @return : 이전 메세지 목록
     */
    @Override
    public List<ChatMessageModel> getMessages(String roomId, int page, int size) {
        long Start = page * size;
        long end = (page+1) * size - 1;

        List<Object> messages = listOps.range("chat:rooms:" + roomId, Start, end);


        ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        List<ChatMessageModel> typedMessages = messages.stream()
                .map(message -> mapper.convertValue(message, ChatMessageModel.class))
                .collect(Collectors.toList());

        return typedMessages;
    }

    /**
     * 사용자가 속한 채팅방 목록을 조회합니다. (채팅방 ID 목록을 조회하고, 채팅방 정보를 조회하여 반환)
     *
     * @param userId : 사용자 ID
     * @return : 사용자가 속한 채팅방 목록
     */
    @Override
    public List<ChatRoomsResponseDTO> getUserRooms(String userId) {
        // 사용자가 속한 채팅방 ID 목록을 조회
        Set<Object> roomIds = redisTemplate.opsForSet().members("user:rooms:" + userId);


        // 채팅방 ID 목록으로 채팅방 정보 및 마지막 메시지 조회
        ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

        return roomIds.stream()
                .map(roomId -> {
                    ChatRoomModel chatRoom = mapper.convertValue(hashOps.get("chat:rooms", roomId), ChatRoomModel.class);
                    ChatMessageModel chatMessageModel = mapper.convertValue(hashOps.get("chat:rooms:lastMessage", roomId), ChatMessageModel.class);

                    // ChatRoomModel과 ChatMessageModel을 ChatRoomsResponseDTO로 변환
                    return (chatRoom != null && chatMessageModel != null)
                    ? ChatRoomsResponseDTO.of(
                            chatRoom.getRoomId(),
                            chatRoom.getParticipantOne(),
                            chatRoom.getParticipantTwo(),
                            chatMessageModel
                    ) : null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}