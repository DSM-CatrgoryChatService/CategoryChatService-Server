package com.example.chat_example_with_socketio.controller;

import com.example.chat_example_with_socketio.payload.response.SoloRoomResponse;
import com.example.chat_example_with_socketio.service.solo_chat_room.SoloChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SoloChatRoomController {

    private final SoloChatRoomService service;

    @GetMapping("/solo")
    public List<SoloRoomResponse> getSolo(@RequestHeader("Authorization") String token,
                                          @RequestBody PageRequest pageRequest) {
        return service.getChatRoom(token, pageRequest);
    }
}
