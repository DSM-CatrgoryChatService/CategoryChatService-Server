package com.example.chat_example_with_socketio.payload.request;

import lombok.Getter;

@Getter
public class MessageRequest {
    private String chatId;
    private String message;
}
