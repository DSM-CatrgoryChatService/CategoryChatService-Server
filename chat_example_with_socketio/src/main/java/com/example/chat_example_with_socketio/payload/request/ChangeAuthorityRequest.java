package com.example.chat_example_with_socketio.payload.request;

import com.example.chat_example_with_socketio.entity.chat_room.enums.Authority;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChangeAuthorityRequest {
    private String chatId;
    private String userInfo;
    private Authority authority;
}
