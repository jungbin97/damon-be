package damon.backend.service;

import damon.backend.dto.request.chatting.ChatMessageRequestDTO;
import damon.backend.dto.response.chatting.ChatRoomsResponseDTO;
import damon.backend.model.ChatMessageModel;

import java.util.List;

public interface ChatService {

    String createChatRoom(String currentUserId, String opponentId);

    ChatMessageModel saveMessage(String roomId, ChatMessageRequestDTO chatMessage);

    List<ChatMessageModel> getMessages(String roomId, int page, int size);

    List<ChatRoomsResponseDTO> getUserRooms(String identifier);
}
