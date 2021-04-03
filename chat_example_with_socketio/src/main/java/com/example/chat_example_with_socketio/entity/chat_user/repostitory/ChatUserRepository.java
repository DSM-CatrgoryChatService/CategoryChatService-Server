package com.example.chat_example_with_socketio.entity.chat_user.repostitory;

import com.example.chat_example_with_socketio.entity.chat_user.ChatUser;
import org.springframework.data.repository.CrudRepository;

public interface ChatUserRepository extends CrudRepository<ChatUser, Integer> {
}
