package com.example.chat_example_with_socketio.controller;

import com.example.chat_example_with_socketio.payload.request.UpdateMessageRequest;
import com.example.chat_example_with_socketio.payload.response.MessageResponse;
import com.example.chat_example_with_socketio.service.solo_message.SoloMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/solomessage")
@RequiredArgsConstructor
public class SoloMessageController {

    private final SoloMessageService service;

    @GetMapping("/{chatId}")
    public List<MessageResponse> getMessage(@RequestHeader("Authorization") String token,
                                            @PathVariable String chatId,
                                            @RequestBody PageRequest pageRequest) {
        return service.getMessages(token, chatId, pageRequest);
    }

    @PutMapping
    public void updateMessage(@RequestHeader("Authorization") String token,
                              @RequestBody UpdateMessageRequest updateMessageRequest) {
        service.updateMessage(token, updateMessageRequest);
    }

    @DeleteMapping
    public void deleteMessage(@RequestHeader("Authorization") String token,
                              @RequestParam Integer messageId,
                              @RequestParam String chatId) {
        service.deleteMessage(token, messageId, chatId);
    }
}
