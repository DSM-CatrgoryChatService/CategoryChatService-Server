package com.example.chat_example_with_socketio.entity.chat_room;

import com.example.chat_example_with_socketio.entity.chat_room.enums.Authority;
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
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String chatId;

    private String title;

    private Integer userId;

    private String userInfo;

    private Authority authority;

    private String category;

    private String deviceToken;

    public ChatRoom update(String title) {
        this.title = title;

        return this;
    }

    public ChatRoom giveAuthority(Authority authority) {
        this.authority = authority;

        return this;
    }
}
