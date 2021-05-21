package com.example.chat_example_with_socketio.entity.chat.repository;

import com.example.chat_example_with_socketio.entity.chat.Chat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRepository extends CrudRepository<Chat, Integer> {
    Optional<Chat> findByUserIdAndChatId(Integer userId, String chatId);
    Optional<Chat> findByChatIdOrderByWriteAtDesc(String chatId);
    List<Chat> findAllByChatId(String chatId);
    Optional<Chat> findByChatIdAndId(String chatId, Integer id);
    Page<Chat> findAllByChatId(String chatId, Pageable pageable);
}
