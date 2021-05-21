package com.example.chat_example_with_socketio.controller;

import com.example.chat_example_with_socketio.payload.response.FriendsResponse;
import com.example.chat_example_with_socketio.service.friends.FriendsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/friends")
public class FriendsController {

    private final FriendsService friendsService;

    @GetMapping
    public List<FriendsResponse> getFriends(@RequestHeader("Authorization") String token) {
        return friendsService.getFriends(token);
    }

    @GetMapping("/block")
    public List<FriendsResponse> getBlockFriends(@RequestHeader("Authorization") String token) {
        return friendsService.getBlockFriends(token);
    }

    @PostMapping
    public void addFriend(@RequestHeader("Authorization") String token,
                          @RequestParam String userInfo) {
        friendsService.addFriends(token, userInfo);
    }

    @PutMapping
    public void blockFriend(@RequestHeader("Authorization") String token,
                          @RequestParam String userInfo,
                          @RequestParam boolean isFriendly) {
        friendsService.blockFriend(token, userInfo, isFriendly);
    }

    @DeleteMapping
    public void deleteFriend(@RequestHeader("Authorization") String token,
                              @RequestParam String userInfo) {
        friendsService.deleteFriends(token, userInfo);
    }
}
