package com.example.chat_example_with_socketio.entity.user.repository;

import com.example.chat_example_with_socketio.entity.user.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    Optional<User> findById(String id);
    Optional<User> findByUserId(Integer userId);
    Optional<User> findByInfo(String info);
}
