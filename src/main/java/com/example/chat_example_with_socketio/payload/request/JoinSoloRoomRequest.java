package com.example.chat_example_with_socketio.payload.request;

import lombok.Getter;

@Getter
public class JoinSoloRoomRequest {
    private String chatId;
    private String friendInfo;
}
