package com.example.chat_example_with_socketio.service;

import com.example.chat_example_with_socketio.payload.request.SearchMessageRequest;
import com.example.chat_example_with_socketio.payload.request.UpdateMessageRequest;
import com.example.chat_example_with_socketio.payload.response.ChatRoomResponse;
import com.example.chat_example_with_socketio.payload.response.MessageResponse;

import java.util.List;

public interface MessageService {
    List<MessageResponse> getMessages(String token, String chatId);
    void deleteMessage(String token, Integer messageId, String chatId);
    void updateMessage(String token, UpdateMessageRequest updateMessageRequest);
}
