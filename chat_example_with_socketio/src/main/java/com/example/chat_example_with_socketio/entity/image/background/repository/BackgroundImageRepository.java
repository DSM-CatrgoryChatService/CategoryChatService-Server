package com.example.chat_example_with_socketio.entity.image.background.repository;

import com.example.chat_example_with_socketio.entity.image.background.BackgroundImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BackgroundImageRepository extends JpaRepository<BackgroundImage, Integer> {
    Optional<BackgroundImage> findByUserId(Integer userId);
    Optional<BackgroundImage> findByImageName(String imageName);
}
