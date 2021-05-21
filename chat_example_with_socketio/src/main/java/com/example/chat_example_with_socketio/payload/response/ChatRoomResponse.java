package com.example.chat_example_with_socketio.payload.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatRoomResponse {
    private String chatId;
    private String title;
    private String category;
    private String topMessage;
    private String imageName;
}
