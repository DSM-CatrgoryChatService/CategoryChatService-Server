package com.example.chat_example_with_socketio.entity.user.repository;

import com.example.chat_example_with_socketio.entity.user.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Integer> {
    Optional<User> findById(String id);
    Optional<User> findByUserId(Integer userId);
}
