package com.example.chat_example_with_socketio.service.solo_chat_room;

import com.example.chat_example_with_socketio.payload.response.SoloRoomResponse;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface SoloChatRoomService {
    List<SoloRoomResponse> getChatRoom(String token, PageRequest pageRequest);
}
