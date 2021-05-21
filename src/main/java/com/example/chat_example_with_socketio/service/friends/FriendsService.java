package com.example.chat_example_with_socketio.service.friends;

import com.example.chat_example_with_socketio.payload.response.FriendsResponse;

import java.util.List;

public interface FriendsService {
    void addFriends(String token, String userInfo);
    List<FriendsResponse> getFriends(String token);
    List<FriendsResponse> getBlockFriends(String token);
    void blockFriend(String token, String userInfo, boolean isFriendly);
    void deleteFriends(String token, String userInfo);
}
