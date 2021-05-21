package com.example.chat_example_with_socketio.controller;

import com.example.chat_example_with_socketio.entity.chat_room.enums.Authority;
import com.example.chat_example_with_socketio.payload.request.ChangeAuthorityRequest;
import com.example.chat_example_with_socketio.payload.request.ChangeTitleRequest;
import com.example.chat_example_with_socketio.payload.response.ChatRoomResponse;
import com.example.chat_example_with_socketio.service.chat_room.ChatRoomService;
import com.example.chat_example_with_socketio.service.chat_room.ChatRoomServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/chatroom")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @GetMapping
    public List<ChatRoomResponse> getChatRoomList(@RequestHeader("Authorization") String token,
                                                  @RequestBody PageRequest pageRequest) {
        return chatRoomService.getChatRoom(token, pageRequest);
    }

    @GetMapping("/search")
    public List<ChatRoomResponse> searchWittCategory(@RequestHeader("Authorization") String token,
                                                     @RequestParam String chatCategory,
                                                     @RequestBody PageRequest pageRequest) {
        return chatRoomService.searchWithCategory(token, chatCategory, pageRequest);
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

    @PutMapping("/give")
    public void giveAuthority(@RequestHeader("Authorization") String token,
                              @RequestParam String chatId,
                              @RequestParam String userInfo,
                              @RequestParam Authority authority) {
        chatRoomService.updateAuthority(token,
                ChangeAuthorityRequest.builder()
                        .authority(authority)
                        .userInfo(userInfo)
                        .chatId(chatId)
                        .build()
                );
    }

    @DeleteMapping("/{chatId}")
    public void exitRoom(@PathVariable String chatId,
                         @RequestHeader("Authorization") String token) {
        chatRoomService.exitRoom(chatId, token);
    }
}
