package com.example.chat_example_with_socketio.payload.request;

import lombok.Getter;

@Getter
public class ChangeTitleRequest {
    private String title;
    private String chatId;
}
