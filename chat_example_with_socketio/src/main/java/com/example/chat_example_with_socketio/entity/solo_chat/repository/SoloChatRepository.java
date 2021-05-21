package com.example.chat_example_with_socketio.entity.solo_chat.repository;


import com.example.chat_example_with_socketio.entity.chat.Chat;
import com.example.chat_example_with_socketio.entity.solo_chat.SoloChat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SoloChatRepository extends JpaRepository<SoloChat, Integer> {
    void deleteByChatId(String chatId);
    Optional<SoloChat> findByChatIdOrderByWriteAtDesc(String chatId);
    Page<SoloChat> findAllByChatId(String chatId, Pageable pageable);
    Optional<SoloChat> findByChatIdAndId(String chatId, Integer id);
}
