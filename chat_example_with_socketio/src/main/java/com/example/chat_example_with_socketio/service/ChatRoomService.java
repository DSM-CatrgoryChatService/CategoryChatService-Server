package com.example.chat_example_with_socketio.service;

import com.example.chat_example_with_socketio.entity.chat_room.enums.ChatCategory;
import com.example.chat_example_with_socketio.payload.request.ChangeTitleRequest;
import com.example.chat_example_with_socketio.payload.response.ChatRoomResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ChatRoomService {
    List<ChatRoomResponse> getChatRoom(String token);
    List<ChatRoomResponse> searchWithCategory(String token, ChatCategory chatCategory);
    void updateTitle(ChangeTitleRequest changeTitleRequest, String token);
    void exitRoom(String chatId, String token);
    void changeRoomImage(String chatId, String token, MultipartFile image);
}
