package com.example.chat_example_with_socketio.entity.image.user.repository;

import com.example.chat_example_with_socketio.entity.image.user.UserImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserImageRepository extends JpaRepository<UserImage, Integer> {
    Optional<UserImage> findByUserId(Integer userId);
    Optional<UserImage> findByImageName(String imageName);
    void deleteByImageName(String imageName);
}
