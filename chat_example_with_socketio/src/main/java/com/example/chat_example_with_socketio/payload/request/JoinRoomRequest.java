package com.example.chat_example_with_socketio.payload.request;

import lombok.Getter;

@Getter
public class JoinRoomRequest {
    private String chatId;
    private String title;
}
