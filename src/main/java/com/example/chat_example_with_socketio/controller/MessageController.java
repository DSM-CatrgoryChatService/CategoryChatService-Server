package com.example.chat_example_with_socketio.controller;

import com.example.chat_example_with_socketio.payload.request.UpdateMessageRequest;
import com.example.chat_example_with_socketio.payload.response.MessageResponse;
import com.example.chat_example_with_socketio.service.message.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/message")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @GetMapping("/{chatId}")
    public List<MessageResponse> getMessages(@RequestHeader("Authorization") String token,
                                             @PathVariable String chatId,
                                             @RequestParam PageRequest pageRequest) {
        return messageService.getMessages(token, chatId, pageRequest);
    }

    @PutMapping
    public void updateMessage(@RequestHeader("Authorization") String token,
                              @RequestBody UpdateMessageRequest updateMessageRequest) {
        messageService.updateMessage(token, updateMessageRequest);
    }

    @DeleteMapping("/{chatId}")
    public void deleteMessage(@RequestHeader("Authorization") String token,
                              @RequestParam Integer messageId,
                              @PathVariable String chatId) {
        messageService.deleteMessage(token, messageId, chatId);
    }
}
