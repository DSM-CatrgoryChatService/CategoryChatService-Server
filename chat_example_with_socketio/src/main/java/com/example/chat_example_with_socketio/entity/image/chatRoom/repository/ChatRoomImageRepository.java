package com.example.chat_example_with_socketio.entity.image.chatRoom.repository;

import com.example.chat_example_with_socketio.entity.image.chatRoom.ChatRoomImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRoomImageRepository extends JpaRepository<ChatRoomImage, String> {
    Optional<ChatRoomImage> findByChatId(String chatId);
    void deleteByChatId(String chatId);
}
