package com.example.chat_example_with_socketio.service.socket;

import com.corundumstudio.socketio.SocketIOClient;
import com.example.chat_example_with_socketio.payload.request.*;
import com.example.chat_example_with_socketio.payload.response.ChatRoomResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SocketService {
    void connect(SocketIOClient client);
    void disconnect(SocketIOClient client);
    void joinRoom(SocketIOClient client, JoinChatRoomRequest joinChatRoomRequest);
    void leaveRoom(SocketIOClient client, String chatId);
    void sendMessage(SocketIOClient client, MessageRequest messageRequest);
    void changeTitle(SocketIOClient client, ChangeTitleRequest changeTitleRequest);
    void changeChatRoomImage(SocketIOClient client, String chatId);
    void changeMessage(SocketIOClient client, String chatId);
    void giveAuthority(SocketIOClient client, ChangeAuthorityRequest changeAuthorityRequest);

    void joinSoloRoom(SocketIOClient client, JoinSoloRoomRequest joinSoloRoomRequest);
    void leaveSoloRoom(SocketIOClient client, String chatId);
    void sendSoloMessage(SocketIOClient client, MessageRequest messageRequest);
}
