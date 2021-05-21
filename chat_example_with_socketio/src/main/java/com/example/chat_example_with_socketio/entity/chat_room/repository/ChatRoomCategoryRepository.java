package com.example.chat_example_with_socketio.entity.chat_room.repository;

import com.example.chat_example_with_socketio.entity.chat_room.ChatRoomCategory;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRoomCategoryRepository extends JpaRepository<ChatRoomCategory, String> {
    List<ChatRoomCategory> findAll();
}
