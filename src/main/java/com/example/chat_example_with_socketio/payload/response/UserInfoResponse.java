package com.example.chat_example_with_socketio.payload.response;

import com.example.chat_example_with_socketio.entity.user.enums.Sex;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserInfoResponse {
    private String name;
    private String myCode;
    private Sex sex;
    private String backgroundImageName;
    private String profileImageName;
}
