package com.example.chat_example_with_socketio.entity.friends.repository;

import com.example.chat_example_with_socketio.entity.friends.Friends;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendsRepository extends JpaRepository<Friends, Integer> {
    List<Friends> findAllByUserId(Integer userId);
    Optional<Friends> findByFriendIdAndUserId(Integer friendId, Integer userId);
    void deleteByFriendIdAndUserId(Integer friendId, Integer userId);
}
