package com.example.chat_example_with_socketio.entity.chat_room.repository;

import com.example.chat_example_with_socketio.entity.chat_room.ChatRoom;
import com.example.chat_example_with_socketio.entity.chat_room.enums.ChatCategory;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends CrudRepository<ChatRoom, String> {
    Optional<ChatRoom> findByChatRoomIdAndUserId(String chatId, Integer userId);
    ChatRoom findByChatRoomId(String chatId);
    boolean existsByChatRoomId(String chatId);
    ChatRoom findTop1ByChatRoomId(String chatId);
    List<ChatRoom> findAllByUserId(Integer userId);
    void deleteByChatRoomIdAndUserId(String chatRoomId, Integer userId);
    List<ChatRoom> findAllByChatCategory(ChatCategory chatCategory);
}
