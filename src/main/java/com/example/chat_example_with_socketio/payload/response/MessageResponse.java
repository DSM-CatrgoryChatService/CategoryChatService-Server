package com.example.chat_example_with_socketio.payload.response;

import com.example.chat_example_with_socketio.entity.chat.enums.MessageType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class MessageResponse {
    private Integer messageId;
    private String chatId;
    private String name;
    private String messge;
    private MessageType messageType;
    private LocalDateTime writeAt;
    private String imageName;
    private boolean isMine;
}
