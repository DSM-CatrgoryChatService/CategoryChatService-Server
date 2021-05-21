package com.example.chat_example_with_socketio.payload.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SoloRoomResponse {
    private String chatId;
    private String friendName;
    private String friendImageName;
    private String topMessage;
}
