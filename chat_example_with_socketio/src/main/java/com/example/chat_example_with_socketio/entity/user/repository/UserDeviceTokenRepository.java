package com.example.chat_example_with_socketio.entity.user.repository;

import com.example.chat_example_with_socketio.entity.user.UserDeviceToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserDeviceTokenRepository extends JpaRepository<UserDeviceToken, Integer> {
    UserDeviceToken findByUserId(Integer userId);
}
