package com.example.chat_example_with_socketio.service.message;

import com.example.chat_example_with_socketio.payload.request.UpdateMessageRequest;
import com.example.chat_example_with_socketio.payload.response.MessageResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MessageService {
    List<MessageResponse> getMessages(String token, String chatId, Pageable pageable);
    void deleteMessage(String token, Integer messageId, String chatId);
    void updateMessage(String token, UpdateMessageRequest updateMessageRequest);
}
