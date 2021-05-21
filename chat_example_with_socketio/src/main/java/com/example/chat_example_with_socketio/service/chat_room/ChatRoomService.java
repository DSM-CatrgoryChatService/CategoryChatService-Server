package com.example.chat_example_with_socketio.service.chat_room;

import com.example.chat_example_with_socketio.payload.request.ChangeAuthorityRequest;
import com.example.chat_example_with_socketio.payload.request.ChangeTitleRequest;
import com.example.chat_example_with_socketio.payload.response.ChatRoomResponse;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ChatRoomService {
    List<ChatRoomResponse> getChatRoom(String token, PageRequest pageRequest);
    List<ChatRoomResponse> searchWithCategory(String token, String chatCategory, PageRequest pageRequest);
    void updateAuthority(String token, ChangeAuthorityRequest changeAuthorityRequest);
    void updateTitle(ChangeTitleRequest changeTitleRequest, String token);
    void exitRoom(String chatId, String token);
    void changeRoomImage(String chatId, String token, MultipartFile image);
}
