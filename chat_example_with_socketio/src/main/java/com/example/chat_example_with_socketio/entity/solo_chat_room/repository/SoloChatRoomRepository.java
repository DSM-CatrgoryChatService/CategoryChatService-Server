package com.example.chat_example_with_socketio.entity.solo_chat_room.repository;

import com.example.chat_example_with_socketio.entity.solo_chat_room.SoloChatRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SoloChatRoomRepository extends JpaRepository<SoloChatRoom, String> {
    Optional<SoloChatRoom> findByChatIdAndUserId(String chatId, Integer userId);
    Optional<SoloChatRoom> findByUserIdAndFriendsInfo(Integer userId, String friendsInfo);
    void deleteByChatId(String chatId);
    Page<SoloChatRoom> findAllByUserId(Integer userId, Pageable pageable);

}
