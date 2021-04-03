package com.example.chat_example_with_socketio.controller;

import com.example.chat_example_with_socketio.entity.chat_room.enums.ChatCategory;
import com.example.chat_example_with_socketio.payload.request.ChangeTitleRequest;
import com.example.chat_example_with_socketio.payload.response.ChatRoomResponse;
import com.example.chat_example_with_socketio.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/chatroom")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @GetMapping
    public List<ChatRoomResponse> getChatRoomList(@RequestHeader("Authorization") String token) {
        return chatRoomService.getChatRoom(token);
    }

    @GetMapping("/search")
    public List<ChatRoomResponse> searchWittCategory(@RequestHeader("Authorization") String token,
                                                     @RequestParam ChatCategory chatCategory) {
        return chatRoomService.searchWithCategory(token, chatCategory);
    }

    @PutMapping
    public void updateTitle(@RequestHeader("Authorization") String token,
                            @RequestBody ChangeTitleRequest changeTitleRequest) {
        chatRoomService.updateTitle(changeTitleRequest, token);
    }

    @PutMapping("/image/{chatId}")
    public void updateImage(@PathVariable String chatId,
                            @RequestHeader("Authorization") String token,
                            @RequestParam MultipartFile image) {
        chatRoomService.changeRoomImage(chatId, token, image);
    }

    @DeleteMapping("/{chatId}")
    public void exitRoom(@PathVariable String chatId,
                         @RequestHeader("Authorization") String token) {
        chatRoomService.exitRoom(chatId, token);
    }
}
