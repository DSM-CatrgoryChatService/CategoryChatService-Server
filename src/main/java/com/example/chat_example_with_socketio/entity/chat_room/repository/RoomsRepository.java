package com.example.chat_example_with_socketio.entity.chat_room.repository;

import com.example.chat_example_with_socketio.entity.chat_room.Rooms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomsRepository extends JpaRepository<Rooms, String> {
    Rooms findByChatId(String chatId);
}
