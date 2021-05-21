package com.example.chat_example_with_socketio.entity.chat_room.repository;

import com.example.chat_example_with_socketio.entity.chat_room.ChatRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends CrudRepository<ChatRoom, String> {
    Optional<ChatRoom> findByChatIdAndUserId(String chatId, Integer userId);
    Optional<ChatRoom> findByUserInfoAndChatId(String userInfo, String chatId);
    ChatRoom findByChatId(String chatId);
    boolean existsByChatId(String chatId);
    ChatRoom findTop1ByChatId(String chatId);
    Page<ChatRoom> findAllByUserId(Integer userId, Pageable pageable);
    void deleteByChatIdAndUserId(String chatRoomId, Integer userId);
    List<ChatRoom> findAllByChatId(String chatId);
    Page<ChatRoom> findAllByCategory(String category, Pageable pageable);
}
