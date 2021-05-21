package com.example.chat_example_with_socketio.payload.request;

import lombok.Getter;

@Getter
public class JoinChatRoomRequest {
    private String chatId;
    private String chatCategory;
}
