package com.example.chat_example_with_socketio.entity.token;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Token {
    @Id
    Integer userId;

    String refreshToken;

    public Token update(String refreshToken) {
        this.refreshToken = refreshToken;

        return this;
    }
}
