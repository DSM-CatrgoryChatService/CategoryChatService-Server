package com.example.chat_example_with_socketio.entity.token.repository;

import com.example.chat_example_with_socketio.entity.token.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Integer> {
    Optional<Token> findByUserId(Integer userId);
    Optional<Token> findByRefreshToken(String refreshToken);
}
