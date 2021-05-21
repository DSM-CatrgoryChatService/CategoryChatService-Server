package com.example.chat_example_with_socketio.payload.response;

import com.example.chat_example_with_socketio.entity.user.enums.Sex;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FriendsResponse {
    private String friendsInfo;
    private String name;
    private Sex sex;
    private String userImageName;
    private String backgroundImageName;
    private boolean isFriendly;
    private boolean isMine;
}
