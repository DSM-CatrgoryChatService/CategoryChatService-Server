package com.example.chat_example_with_socketio.payload.request;

import lombok.Getter;

@Getter
public class SearchMessageRequest {
    private String message;
    private String chatId;
}
